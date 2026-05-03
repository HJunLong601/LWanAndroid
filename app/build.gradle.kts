import com.android.build.api.variant.impl.VariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("therouter")
    id("com.google.dagger.hilt.android")
//    id("com.hjl.plugin")
}

android {

    compileSdk = Android.compileSdkVersion

    defaultConfig {
        applicationId = "com.hjl.lwanandroid"
        minSdk = Android.minSdkVersion
        targetSdk = Android.targetSdkVersion
        versionCode = 2
        versionName = "1.0.1"
    }

    signingConfigs {
//        create("release") {
//            storeFile = File(properties["RELEASE_STORE_FILE"] as String)
//            storePassword = properties["RELEASE_STORE_PASSWORD"] as String
//            keyAlias = properties["RELEASE_KEY_ALIAS"] as String
//            keyPassword = properties["RELEASE_KEY_PASSWORD"] as String
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    lint {
        disable += "NotificationPermission"
    }

    buildTypes {
        val debug = getByName("debug")
        debug.apply {
            buildConfigField("boolean", "isDebug", "true")
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["APP_NAME"] = "玩安卓测试版"
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
    namespace = "com.hjl.lwanandroid"
}

androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        val versionCode = android.defaultConfig.versionName ?: 1
        val apkName = "WanAndroid-${variant.buildType}-$versionCode.apk"
        variant.outputs.forEach { output ->
            (output as VariantOutputImpl).outputFileName.set(apkName)
        }
    }
}

fun buildTime(): String {
    val date = Date(System.currentTimeMillis())
    val sdf = SimpleDateFormat("MM_dd_HH_mm", Locale.getDefault())
    return sdf.format(date)
}

fun String?.toBooleanFlag(): Boolean {
    return this.equals("true", ignoreCase = true)
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    if (!(properties["isModule"] as String).toBooleanFlag()) {
        implementation(project(":module_core"))
    }

    if (properties["isEnableSkin"].toString().toBooleanFlag()) {
        implementation(project(":module_func:func_skin"))
    }
    kapt(Jetpack.hilt_compiler)

    implementation(Jetpack.hilt)
    implementation(project(":module_func:func_language"))
}
