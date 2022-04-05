package com.hjl.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.CannotCompileException
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassWriter
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class ApplicationTransform extends Transform {
    Project project
    def targetName = "com.hjl.wanandroid.TestApplication"
    def originName = "com.hjl.wanandroid.WanApplication"

    ApplicationTransform(Project project) {
        this.project = project
    }


    @Override
    String getName() {
        return "ApplicationTransform"
    }

      /**
      * 需要处理的数据类型，目前 ContentType 有六种枚举类型，通常我们使用比较频繁的有前两种：
      *      1、CONTENT_CLASS：表示需要处理 java 的 class 文件。
      *      2、CONTENT_JARS：表示需要处理 java 的 class 与 资源文件。
      *      3、CONTENT_RESOURCES：表示需要处理 java 的资源文件。
      *      4、CONTENT_NATIVE_LIBS：表示需要处理 native 库的代码。
      *      5、CONTENT_DEX：表示需要处理 DEX 文件。
      *      6、CONTENT_DEX_WITH_RESOURCES：表示需要处理 DEX 与 java 的资源文件。
      *
      * @return
      */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

     /**
      * 表示 Transform 要操作的内容范围，目前 Scope 有五种基本类型：
      *      1、PROJECT                   只有项目内容
      *      2、SUB_PROJECTS              只有子项目
      *      3、EXTERNAL_LIBRARIES        只有外部库
      *      4、TESTED_CODE               由当前变体（包括依赖项）所测试的代码
      *      5、PROVIDED_ONLY             只提供本地或远程依赖项
      *      SCOPE_FULL_PROJECT 是一个 Scope 集合，包含 Scope.PROJECT, Scope.SUB_PROJECTS, Scope.EXTERNAL_LIBRARIES 这三项，即当前 Transform 的作用域包括当前项目、子项目以及外部的依赖库
      *
      * @return
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
            throws IOException, TransformException, InterruptedException {


        File originClass, targetClass, extractRoot
        println "get appname $originName"
        String path
        // Transform的inputs有两种类型，一种是项目内的目录，一种是第三方的jar包，要分开遍历
        inputs.each { TransformInput input ->
            //对类型为“directory” 的 input 进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->
                println directoryInput.file.name
                path = directoryInput.file.absolutePath
                def originFile = new File(directoryInput.file,originName.toString().replace('.', File.separator)+".class")
                def targetFile = new File(directoryInput.file,targetName.toString().replace('.', File.separator)+".class")

                if (originFile.exists()){
                    println "find origin file from dir input"
                    originClass = originFile
                }

                if(targetFile.exists()){
                    println "find target file from dir input"
                    targetClass = targetFile
                }

                // 获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                extractRoot = dest
                println "tatget:${targetClass},originClass:${originClass}"

                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //对第三方的 jar 包文件，进行遍历
            input.jarInputs.each { JarInput jarInput ->
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                //生成输出路径
                def dest = outputProvider.getContentLocation(md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)

                // 将input的目录复制到output指定目录
                FileUtils.copyFile(jarInput.file, dest)

                if (dest.exists()) {
                    if (JarZipUtil.containsEntry(dest, targetName.replace('.', '/') + '.class')){
                        println "find target file from jar input"
                        targetFile = dest
                        path.add(dest.absolutePath)
                    }

                    if (JarZipUtil.containsEntry(dest, originName.replace('.', '/') + '.class')){
                        println "find origin file from jar input"
                        originFile = dest
                        path.add(dest.absolutePath)
                    }

                }
            }
        }


        println "ready to add path:${path.toString()}"
        println "get origin file: $originFile"
        println "get target file: $targetFile"
        modifySuper(path,extractRoot.absolutePath)
        println "=================Cost time : ${System.currentTimeMillis() - startTime} =============="

    }

    boolean modifySuper(String workDir, String targetPath){
        ClassPool pool = ClassPool.getDefault()



        def currentCP = []
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        pool.appendClassPath(workDir)


        CtClass c = pool.getCtClass(originName)
        if (c.isFrozen()) {
            c.defrost()
        }

        pool.importPackage(targetName)
        c.setSuperclass(pool.getCtClass(targetName))
        c.getDeclaredMethods().each { CtMethod cm ->
            cm.instrument(new ExprEditor() {
                @Override
                void edit(MethodCall m) throws CannotCompileException {
                    println "m.className:$m.className ,methodName:$m.methodName"
                    println "cm.methodName:$cm.name"

                    // $_代表的是方法的返回值 $$是所有方法参数的简写
                    if (m.className == 'android.app.Application' && m.methodName == cm.name) {
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

        currentCP.each {
            pool.removeClassPath(it)
        }
    }

}