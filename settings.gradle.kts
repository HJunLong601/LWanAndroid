include(":app")
include(":commonlib")
include(":module_base")
include(":module_core")
include(":jetpacklib")
include(":module_func:func_skin")

include(":skin:skin-dark")
include(":skin:skin-green")
include(":skin:skin-yellow")

rootProject.name = "LWanAndroid"



//gradle.addBuildListener(new BuildListener() {
//    @Override
//    void buildStarted(Gradle gradle) {
//        println "---- Build Start ----"
//    }
//
//    @Override
//    void settingsEvaluated(Settings settings) {
//        println "---- Evaluate Setting Finish ----"
//    }
//
//    @Override
//    void projectsLoaded(Gradle gradle) {
//        println "---- Load Project Finish ----"
//        println "Init Finish,Root Project ：" + gradle.gradle.rootProject
//    }
//
//    @Override
//    void projectsEvaluated(Gradle gradle) {
//        println "---- Evaluated Project Finish ----"
//    }
//
//    @Override
//    void buildFinished(BuildResult result) {
//        println "---- Build Finish ----"
//    }
//})

