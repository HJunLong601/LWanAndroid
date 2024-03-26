plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}


android {
    compileSdk = Android.compileSdkVersion
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        enable = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        val release = getByName("release")
        release.apply {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    namespace = "com.hjl.module_base"

}

// 2、kotlin 配置ARouter
//kapt {
//    arguments {
//        arg("AROUTER_MODULE_NAME", project.getName())
//    }
//}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":commonlib"))
    api(project(":jetpacklib"))

    api(Dependencies.permissionx)
    api(Dependencies.liveEventBus)

    // room
    api(Jetpack.room)
    kapt(Jetpack.room_compiler)
    api(Jetpack.room_ktx)

}
