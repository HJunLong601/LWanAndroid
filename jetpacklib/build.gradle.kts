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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.compiler
    }

    buildTypes {
        val release = getByName("release")
        release.apply{
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    namespace = "com.hjl.jetpacklib"

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    val composeBom = platform(Compose.bom)

    api(project(":commonlib"))
    api(Jetpack.paging3)
    api(Jetpack.hilt)
    implementation(composeBom)
    implementation(Compose.ui)
    implementation(Compose.foundation)
    implementation(Compose.material3)
    kapt(Jetpack.hilt_compiler)
}

