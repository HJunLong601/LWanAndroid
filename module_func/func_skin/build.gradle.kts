plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = Android.compileSdkVersion
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        minSdk = Android.minSdkVersion
        targetSdk = Android.targetSdkVersion
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        enable = true
    }

    buildTypes {
        val release = getByName("release")
        release.apply{
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    namespace = "com.hjl.skin"

}


dependencies {
//    implementation fileTree(dir: "libs", include: ["*.jar"])

    api(project(":module_base"))

    api(SkinSupport.skin)
    api(SkinSupport.skin_appcompat)
    api(SkinSupport.skin_design)
    api(SkinSupport.skin_cardview)
    api(SkinSupport.skin_constraint)

    kapt(Dependencies.routerApt)


}



