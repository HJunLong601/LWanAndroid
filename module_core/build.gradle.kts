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
        versionCode = 1
        versionName = "1.0"
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

//    /*3.设置模块化时编译模块等;*/
//    sourceSets.main {
//        if (isModule.toBoolean()) {
//            //模块化时;
//            manifest.srcFile "src/moduledebug/AndroidManifest.xml"
//            java.srcDirs = [
//                    "src/moduledebug/java",
//                    "src/main/java"
//            ]
//            res.srcDirs = [
//                    "src/moduledebug/res",
//                    "src/main/res"
//            ]
//            //assets.srcDirs = [] ;
//        } else {
//            manifest.srcFile "src/main/AndroidManifest.xml"
//            java {
//                exclude "src/moduledebug/java/**"  // 不想包含文件的路径
//                exclude "**/package-info.java"
//            }
//            res {
//                exclude "src/moduledebug/res/**" // 不想包含的资源文件路径
//            }
//        }
//    }


}


// 2、kotlin 配置ARouter
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":module_base"))

    implementation(View.GroupedRecyclerViewAdapter)
    implementation(View.banner)
    api(View.flexbox)
    
    api(Jetpack.viewModel)
    kapt(Dependencies.arouter_compiler)
}

