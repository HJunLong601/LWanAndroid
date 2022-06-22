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
//// 2、kotlin 配置ARouter
//kapt {
//    arguments {
//        arg("AROUTER_MODULE_NAME", project.name)
//    }
//}


dependencies {
//    implementation fileTree(dir: "libs", include: ["*.jar"])

    api(Dependencies.bugly)
    api(Dependencies.fastJson)
    api(Dependencies.glide)
    api(Dependencies.glide_transformations)

    // ARouter
    api(Dependencies.arouter)
//    kapt(Dependencies.arouter_compiler)

    // support
    api(AndroidSupport.appcompat)
    api(AndroidSupport.material)
    api(AndroidSupport.lifecycle_extensions)
    api(AndroidSupport.recyclerview)

    // RxJava2
    api(RxJava2.rxjava2)
    api(RxJava2.rxandroid)
    api(RxJava2.rxbinding)

    // Retrofit
    api(NetWork.okhttp)
    api(NetWork.retrofit)
    api(NetWork.retrofit_gson)
    api(NetWork.retrofit_rxjava2)
    api(NetWork.retrofit_scalars)

    // webview
    api(WebView.tbs)

    //view
    api(View.constraintlayout)
    api(View.skeleton)
    api(View.circleimageview)
    api(View.photoView)
    api(View.smartRefreshLayout)
    api(View.shimmerlayout)
    api(View.BRVAH)

    // kotlin && coroutines
    api(KT.ktx)
    api(KT.coroutines)
//    api(KT.kt_stdlib_jdk7)

//    api "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"

//    //内存泄漏检测工具
//    debugApi deps.debug_leakcanary
//    releaseApi deps.release_leakcanary
//    // Optional, if you use support library fragments:
//    debugApi deps.support_fragment_leakcanary


}



repositories {
    mavenCentral()
}
