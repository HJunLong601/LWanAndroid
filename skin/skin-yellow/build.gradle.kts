plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = Android.compileSdkVersion
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        applicationId = "com.hjl.lwanandroid.yellow"
        minSdk = Android.minSdkVersion
        targetSdk = Android.targetSdkVersion
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        val release = getByName("release")
        release.apply{
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    namespace = "com.hjl.skin.yellow"
}

dependencies {

  
}