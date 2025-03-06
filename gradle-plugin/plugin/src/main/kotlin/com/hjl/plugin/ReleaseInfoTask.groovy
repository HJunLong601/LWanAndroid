package com.hjl.plugin;

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReleaseInfoTask extends DefaultTask{

    ReleaseInfoTask(){

        // 1、在构造器中配置 Task 对应的  Task Group 和 描述信息
        group = 'version_manager'
        description = 'release info update'


    }


    // 2、在 gradle 执行阶段执行
    @TaskAction
    void doAction(){

        println("----Begin do action ----")
        updateVersionInfo()
        println("----End do action ----")
    }

    private void updateVersionInfo(){

        // 3、获取相应的版本信息
        println(project.name)
        def versionCodeMsg = project.extensions.releaseInfo.versionCode
        def versionNameMsg = project.extensions.releaseInfo.versionName
        def versionInfoMsg = project.extensions.releaseInfo.versionInfo
        def fileName = project.extensions.releaseInfo.fileName

        def file = project.file(fileName)

        // 4、将实体对象写入文件
        def sw = new StringWriter()
        def xmlBuilder = new MarkupBuilder(sw);

        if (!file.exists()){
            file.createNewFile()
        }

        if (file.text != null && file.text.size() <= 0){
            // 没有内容时
            xmlBuilder.releases{
                release{
                    versionCode(versionCodeMsg)
                    versionName(versionNameMsg)
                    versionInfo(versionInfoMsg)
                }
            }

            file.withWriter { writer ->
                writer.append(sw.toString())
            }
        }else {
            //已有其它版本内容            
            xmlBuilder.release{
                versionCode(versionCodeMsg)
                versionName(versionNameMsg)
                versionInfo(versionInfoMsg)
            }
            // 插入到最后一行前面
            def lines = file.readLines()
            def lengths = lines.size() - 1
            file.withWriter { writer ->
                lines.eachWithIndex { String line, int index ->
                    if (index != lengths){
                        writer.append(line + '\r\n')
                    }else if (index == lengths){
                        writer.append('\n' + sw.toString() + '\n')
                        writer.append(lines.get(lengths))
                    }
                }
            }

        }
    }

}