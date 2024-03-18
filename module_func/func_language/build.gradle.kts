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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        isEnabled = true
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

}


dependencies {
//    implementation fileTree(dir: "libs", include: ["*.jar"])

    api(project(":module_base"))

    kapt(Dependencies.routerApt)


}



