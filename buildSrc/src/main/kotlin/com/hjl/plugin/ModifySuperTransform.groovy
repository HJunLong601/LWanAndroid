package com.hjl.plugin;

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.CannotCompileException
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class ModifySuperTransform extends Transform {
    Project project
    String destName,keyClassName
    File destFile
    List<String> originNameList
    List<String> keyMethodNameList
    Map<String,File> originalMap

    ModifySuperTransform(Project project, List<String> originNameList, String destName,
                         String keyClassName,List<String> keyMethodNameList) {
        this.project = project
        this.originNameList = originNameList
        this.destName = destName
        this.keyClassName = keyClassName
        this.keyMethodNameList = keyMethodNameList
    }


    @Override
    String getName() {
        return "ModifySuperTransform"
    }

      /**
       * 需要处理的数据类型，目前 ContentType 有六种枚举类型，通常我们使用比较频繁的有前两种：
       * 1、CONTENT_CLASS：表示需要处理 java 的 class 文件。
       * 2、CONTENT_JARS：表示需要处理 java 的 class 与 资源文件。
       * 3、CONTENT_RESOURCES：表示需要处理 java 的资源文件。
       * 4、CONTENT_NATIVE_LIBS：表示需要处理 native 库的代码。
       * 5、CONTENT_DEX：表示需要处理 DEX 文件。
       * 6、CONTENT_DEX_WITH_RESOURCES：表示需要处理 DEX 与 java 的资源文件。
       *
       * @return
       */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }


    /**
     * 表示 Transform 要操作的内容范围，目前 Scope 有五种基本类型：
     * 1、PROJECT    只有项目内容
     * 2、SUB_PROJECTS    只有子项目
     * 3、EXTERNAL_LIBRARIES   只有外部库
     * 4、TESTED_CODE     由当前变体（包括依赖项）所测试的代码
     * 5、PROVIDED_ONLY   只提供本地或远程依赖项
     * SCOPE_FULL_PROJECT 是一个 Scope 集合，包含 Scope.PROJECT, Scope.SUB_PROJECTS, Scope.EXTERNAL_LIBRARIES 这三项，即当前 Transform 的作用域包括当前项目、子项目以及外部的依赖库
     *
     * @return
     */

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        // 是否支持增量更新
        // 如果返回 true，TransformInput 会包含一份修改的文件列表
        // 如果返回 false，会进行全量编译，删除上一次的输出内容
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental)
            throws IOException, javax.xml.crypto.dsig.TransformException, InterruptedException {


        File extractRoot

        originalMap = new HashMap<>()
        println "try to find originNameList: ${originNameList.toString()}"
        long startTime = System.currentTimeMillis()
        println "========= start transform from ${originNameList.toString()} to $destName ==========="
        def path = []
        // Transform的inputs有两种类型，一种是项目内的目录，一种是第三方的jar包，要分开遍历
        inputs.each { TransformInput input ->
            //对类型为“directory” 的 input 进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->
                println directoryInput.file.name

                for (String originName : originNameList){
                    def originFile = new File(directoryInput.file,originName.toString().replace('.', File.separator)+".class")
                    if (originFile.exists()){
                        println "find ${originFile} from dir input"
                        originalMap.put(originName,originFile)
                        path.add(directoryInput.file.absolutePath)
                    }
                }

                def targetFile = new File(directoryInput.file,destName.toString().replace('.', File.separator)+".class")
                if(targetFile.exists()){
                    println "find $targetFile from dir input"
                    destFile = targetFile
                    path.add(directoryInput.file.absolutePath)
                }

                // 获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                extractRoot = dest
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //对第三方的 jar 包文件，进行遍历 包括组件化下分出的各个module
            input.jarInputs.each { JarInput jarInput ->

//                handleJarInputs(jarInput,outputProvider)
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                //生成输出路径
                def destJar = outputProvider.getContentLocation(md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)

                FileUtils.copyFile(jarInput.file, destJar)

                if (destJar.exists()) {
                    def tName = destName.replace('.', '/') + '.class'
                    if (JarZipUtil.containsEntry(destJar,tName)){
                        println "find dest $tName from jar input"
                        destFile = destJar
                        path.add(destJar.absolutePath)
                    }

                    for (String originName : originNameList){
                        def oName = originName.replace('.', '/') + '.class'
                        if (JarZipUtil.containsEntry(destJar,oName)){
                            println "find original $oName from jar input"
                            originalMap.put(originName,destJar)
                            path.add(destJar.absolutePath)
                        }
                    }

                }
            }
        }


//        println "ready to add path:${path}"
        println "get origin file: $originalMap"
        println "get target file: $destFile"
        modifySuper(path,extractRoot.absolutePath,originalMap)
        println "=================Cost time : ${System.currentTimeMillis() - startTime} =============="

    }



    boolean modifySuper(List workDir, String targetPath,Map<String,File> originalMap){
        ClassPool pool = ClassPool.getDefault()

        def currentCP = []
        //project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        project.android.bootClasspath.each {
            currentCP += pool.appendClassPath(it.absolutePath)
        }

        workDir.each {
            println "insert path $it"
            currentCP += pool.appendClassPath(it)
        }


        CtClass destCtCls = pool.getCtClass(destName)
        for (String originName : originNameList){
           println originName
           CtClass c = pool.getCtClass(originName)
           if (c.isFrozen()) {
               c.defrost()
           }
//           println "super:${destCtCls.toString()}"
//           println "super:${c.toString()}"
           pool.importPackage(destName)
           c.setSuperclass(destCtCls)
           c.getDeclaredMethods().each { CtMethod cm ->
               cm.instrument(new ExprEditor() {
                   @Override
                   void edit(MethodCall m) throws CannotCompileException {
                       // $_代表的是方法的返回值 $$是所有方法参数的简写
                       if (m.className == keyClassName && keyMethodNameList.contains(m.methodName) && m.methodName == cm.name) {
                           println "m.className:$m.className ,methodName:$m.methodName"
                           println "cm.methodName:$cm.name"
                           if (m.signature.endsWith("V"))
                               m.replace("{super.${m.methodName}(\$\$);}")
                           else
                               m.replace("{\$_ = super.${m.methodName}(\$\$);}")
                       }
                   }
               })
           }

           c.writeFile(targetPath)
           c.detach()
       }

        currentCP.each {
            pool.removeClassPath(it)
        }

        println "start delete old file -- jar"
        for (Map.Entry<String, File> entry : originalMap.entrySet()){
            def originFile = entry.getValue()
            def originName = entry.getKey()

            if (!originFile.getName().endsWith("jar")) continue

            def tempDir = new File(originFile.getParent(),originFile.getName() + "_temp")
            def path = originFile.absolutePath
            println "tempDir:${tempDir.absolutePath}"
            println "originFile:${originFile.getName()}"
            JarZipUtil.unzipJar(path,tempDir.absolutePath)
            if (tempDir.exists()){
                def deleteClass = new File(tempDir,originName.toString().replace('.', File.separator)+".class")
                if (deleteClass.exists()){
                    println "delete class result : ${deleteClass.delete()}"
                    println "delete origin jar: ${originFile.delete()}"
                    JarZipUtil.zipJar(tempDir.absolutePath,path)
                }
                FileUtils.deleteDirectory(tempDir)
            }
        }


        println "success replace $originNameList super class as $destName"
        println "================ end replace application transform =========="

    }

}