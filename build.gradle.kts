buildscript {

    repositories {
        maven(".\\localMaven")
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
        classpath("cn.therouter:plugin:1.2.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}

allprojects {
    repositories {
        maven("${rootDir.absolutePath}\\localMaven")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/nexus/content/groups/public")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        google()
        mavenCentral()
    }
    // Pin appcompat to avoid compatibility issues introduced by skin libraries.
    configurations.all {
        resolutionStrategy {
            force("androidx.appcompat:appcompat:1.2.0")
        }
    }
}
