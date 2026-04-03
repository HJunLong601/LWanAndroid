plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    compileSdk = Android.compileSdkVersion

    defaultConfig {
        minSdk = Android.minSdkVersion
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
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



