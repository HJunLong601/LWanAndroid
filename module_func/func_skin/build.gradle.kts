plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Android.compileSdkVersion)
    buildToolsVersion(Android.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode(1)
        versionName("1.0")
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
        release.apply{
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

}
// kotlin 配置ARouter
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}


dependencies {
//    implementation fileTree(dir: "libs", include: ["*.jar"])

    api(project(":module_base"))

    api(SkinSupport.skin)
    api(SkinSupport.skin_appcompat)
    api(SkinSupport.skin_design)
    api(SkinSupport.skin_cardview)
    api(SkinSupport.skin_constraint)

    kapt(Dependencies.arouter_compiler)


}



