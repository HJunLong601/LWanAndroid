

buildscript{

    val kotlinVersion = "1.7.20"

    repositories {
        maven("https://maven.aliyun.com/repository/central" )
        maven("https://jitpack.io" )
        maven("https://maven.aliyun.com/repository/google" )
        maven("https://maven.aliyun.com/repository/jcenter" )
        maven("https://maven.aliyun.com/nexus/content/groups/public" )
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle-api:7.4.2")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")

    }

}

repositories {
    maven("https://maven.aliyun.com/repository/central" )
    maven("https://jitpack.io" )
    maven("https://maven.aliyun.com/repository/google" )
    maven("https://maven.aliyun.com/repository/jcenter" )
    maven("https://maven.aliyun.com/nexus/content/groups/public" )
    maven("https://maven.aliyun.com/repository/gradle-plugin")
//    mavenCentral()
//    google()
    gradlePluginPortal()
}

plugins {
    `kotlin-dsl`
    id("groovy")
}


dependencies {
    // gradle sdk
    implementation(gradleApi())
    // groovy sdk
    implementation(localGroovy())
    // android build tools
    implementation ("com.android.tools.build:gradle-api:7.4.2")


    // third dependencies
    implementation("org.ow2.asm:asm:7.1")
    implementation("org.ow2.asm:asm-util:7.1")
    implementation("org.ow2.asm:asm-commons:7.1")
    implementation("org.javassist:javassist:3.23.1-GA")
    implementation("commons-io:commons-io:2.4")
    implementation("commons-codec:commons-codec:1.10")

//    implementation(project(":skin-plugin"))
//
//    val booster_version = "4.16.3"
//    annotationProcessor("com.google.auto.service:auto-service:1.0")
//    api("com.didiglobal.booster:booster-api:$booster_version")
//    api("com.didiglobal.booster:booster-transform-asm:$booster_version")
}

tasks.withType(JavaCompile::class.java){
    options.encoding = "UTF-8"
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

