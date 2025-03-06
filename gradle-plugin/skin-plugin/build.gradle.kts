buildscript{

    val kotlinVersion = "1.6.21"

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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
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
    id("kotlin-kapt")
    id("maven-publish")
}

dependencies {
    // gradle sdk
    implementation(gradleApi())
    // groovy sdk
    implementation(localGroovy())

    val booster_version = "4.16.3"
    val kotlinVersion = "1.6.21"
    kapt("com.google.auto.service:auto-service:1.0")
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    api("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    api("com.didiglobal.booster:booster-transform-asm:$booster_version")

}

tasks.withType(JavaCompile::class.java){
    options.encoding = "UTF-8"
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

publishing {

    repositories {
        maven {
            url = uri("..\\..\\localMaven")
            isAllowInsecureProtocol = true
        }
    }


    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.hjl.plugin"
            artifactId = "skin-plugin"
            version = "1.0.0"
            from(components.getByName("java"))
        }
    }

}