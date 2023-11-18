import com.android.aaptcompiler.parseAsBool
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")

    id("com.didiglobal.booster")
    id("therouter")
    id("com.hjl.plugin")
}

android {

    compileSdk = Android.compileSdkVersion
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        applicationId = "com.hjl.lwanandroid"
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
//        create("release") {
//            storeFile(File(properties["RELEASE_STORE_FILE"] as String))
//            storePassword(properties["RELEASE_STORE_PASSWORD"] as String)
//            keyAlias(properties["RELEASE_KEY_ALIAS"] as String)
//            keyPassword(properties["RELEASE_KEY_PASSWORD"] as String)
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        isEnabled = true
    }

    buildTypes {
        val debug = getByName("debug")
        debug.apply {
            buildConfigField("boolean", "isDebug", "true")
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["APP_NAME"] = "玩安卓(测试版)"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            signingConfig = signingConfigs.getByName("release")
        }

        val release = getByName("release")
        release.apply {
            buildConfigField("boolean", "isDebug", "false")
            isMinifyEnabled = false
            manifestPlaceholders["APP_NAME"] = "玩安卓"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            signingConfig = signingConfigs.getByName("release")
        }
    }

//    applicationVariants.all { variant ->
//
//        variant.outputs.all { output ->
//            if (variant.buildType.name == "release"){
//                // 修改输入文件夹
//                variant.getPackageApplicationProvider().get().outputDirectory = new File("${rootDir}\\output")
//                // 修改apk名称
//                outputFileName = "WanAndroid-${variant.buildType.name}-${buildTime()}-${defaultConfig.versionCode}.apk"
//            }
//        }
//
//    }
}

fun buildTime(): String {
    val date = Date(System.currentTimeMillis())
    val sdf = SimpleDateFormat("MM_dd_HH_mm")
    return sdf.format(date)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    if (!parseAsBool(properties["isModule"] as String)!!) {
        implementation(project(":module_core"))
    }

    if (parseAsBool(properties["isEnableSkin"].toString())!!) {
        implementation(project(":module_func:func_skin"))
    }

}