plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

buildscript {
    repositories {
        maven("${rootDir.absolutePath}\\localMaven")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/nexus/content/groups/public")
        google()
        mavenCentral()
    }

    dependencies {
        classpath("cn.therouter:plugin:1.3.2")
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
