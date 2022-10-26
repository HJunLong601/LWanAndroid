
apply(from = "version.gradle")


buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/nexus/content/groups/public")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Android.kotlinVersion}")
        classpath ("com.didiglobal.booster:booster-gradle-plugin:4.4.0")
        classpath ( "com.alibaba:arouter-register:1.0.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io" )
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/nexus/content/groups/public")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
