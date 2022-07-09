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

gradle.addBuildListener(object : BuildListener{
    override fun buildStarted(gradle: Gradle) {
        println("---- Build Start ----")
    }

    override fun settingsEvaluated(settings: Settings) {
        println("---- Evaluate Setting Finish ----")
    }

    override fun projectsLoaded(gradle: Gradle) {
        println("---- Load Project Finish ----")
        println("Init Finish,Root Project ：${gradle.gradle.rootProject}")
    }

    override fun projectsEvaluated(gradle: Gradle) {
        println("---- Evaluated Project Finish ----")
    }

    override fun buildFinished(result: BuildResult) {
        println("---- Build Finish ----")
    }

})

