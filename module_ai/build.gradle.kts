plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val enableLlamaCppNative = providers.gradleProperty("enableLlamaCppNative")
    .map { it.equals("true", ignoreCase = true) }
    .orElse(false)

android {
    namespace = "com.hjl.module_ai"
    compileSdk = Android.compileSdkVersion

    defaultConfig {
        minSdk = Android.minSdkVersion
        buildConfigField(
            "boolean",
            "LLAMA_CPP_NATIVE_ENABLED",
            enableLlamaCppNative.get().toString()
        )
        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters += listOf("arm64-v8a")
        }

        if (enableLlamaCppNative.get()) {
            externalNativeBuild {
                cmake {
                    arguments += "-DCMAKE_BUILD_TYPE=Release"
                    arguments += "-DBUILD_SHARED_LIBS=ON"
                    arguments += "-DLLAMA_BUILD_COMMON=ON"
                    arguments += "-DLLAMA_OPENSSL=OFF"
                    arguments += "-DGGML_NATIVE=OFF"
                    arguments += "-DGGML_BACKEND_DL=ON"
                    arguments += "-DGGML_CPU_ALL_VARIANTS=ON"
                    arguments += "-DGGML_LLAMAFILE=OFF"
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    if (enableLlamaCppNative.get()) {
        externalNativeBuild {
            cmake {
                path("src/main/cpp/CMakeLists.txt")
            }
        }
    }
}

dependencies {
    implementation(KT.ktx)
    implementation(KT.coroutines)
}
