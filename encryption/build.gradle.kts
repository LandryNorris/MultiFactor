import org.jetbrains.kotlin.konan.target.HostManager

val kryptoVersion: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.github.ttypic.swiftklib") version "0.6.4"
}

kotlin {
    androidTarget()
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.compilations {
            if(HostManager.hostIsMac) {
                val main by getting {
                    cinterops {
                        create("Attributes")
                    }
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.soywiz.korlibs.krypto:krypto:$kryptoVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 23
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "io.github.landrynorris.encryption"
}

swiftklib {
    create("Attributes") {
        path = file("src/swift")
        packageName("io.github.landrynorris.encryption.swift")
    }
}
