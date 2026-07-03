import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
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
        commonMain.dependencies {
            implementation(libs.cryptography.provider.optimal)
            implementation(libs.cryptography.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    compileSdk = 36
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
