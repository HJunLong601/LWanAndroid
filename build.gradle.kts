apply(from = "version.gradle")


buildscript {

    repositories {
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/nexus/content/groups/public")
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Android.kotlinVersion}")
        classpath("com.didiglobal.booster:booster-gradle-plugin:4.16.3")
        classpath("cn.therouter:plugin:1.2.1")
    }
}

allprojects {
    repositories {
//        google()
        maven("https://maven.aliyun.com/repository/central")
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/nexus/content/groups/public")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        google()
        mavenCentral()
    }
//    指定androidx版本，防止因为换肤库导致的崩溃
//    https://blog.csdn.net/charlinopen/article/details/126625175
    configurations.all {
        resolutionStrategy {
            force("androidx.appcompat:appcompat:1.2.0")
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

