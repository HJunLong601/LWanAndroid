package com.hjl.plugin;

import com.android.build.gradle.AppExtension
import com.hjl.plugin.ApplicationTransform
import com.hjl.plugin.MethodTransform
import com.hjl.plugin.ModifySuperTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project>{

    def isDebug = true
    def isEnableSkin = false

    @Override
    void apply(Project project) {
//        println("this is Test Plugin,Project name is ${project.name}")
//        println("this is Test Plugin,Project name is ${project.getParent().name}")

        isDebug = project.getProperties().get("isDebug")
        isEnableSkin = project.getProperties().get("isEnableSkin")

        def app = project.extensions.findByType(AppExtension.class)
        println "isEnableSkin:$isEnableSkin"
//        if (Boolean.valueOf(isEnableSkin)){
//            def originalList = new ArrayList<String>()
//            originalList.add("com.hjl.commonlib.base.BaseMultipleActivity")
//            originalList.add("com.hjl.jetpacklib.mvvm.view.BaseActivity")
//
//            def keyMethodNameList = new ArrayList<String>()
//            keyMethodNameList.add("getDelegate")
//            keyMethodNameList.add("onResume")
//            keyMethodNameList.add("onDestroy")

//            def modifySuper = new ModifySuperTransform(
//                    project,
//                    originalList,
//                    "com.hjl.lwanandroid.skin.SkinBaseActivity",
//                    "androidx.appcompat.app.AppCompatActivity",
//                    keyMethodNameList
//            )
//            app.registerTransform(modifySuper)
//        }

//        def applicationTransform = new ApplicationTransform(project)
//        app.registerTransform(applicationTransform)

//        def methodTransform = new MethodTransform(project)
//        app.registerTransform(methodTransform)

        // 创建Extensions属性使app下的build.gradle 可以使用 releaseInfo 这个闭包
       project.extensions.create("releaseInfo",ReleaseInfoExtension.class)

        //创建用于更新版本信息的Task
       project.tasks.create("releaseInfoTask",ReleaseInfoTask.class)

//        isDebug = project.extensions.extraProperties.get("isDebug")



        project.afterEvaluate {
            println("----afterEvaluate-----")
        }

    }
}