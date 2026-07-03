import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
    alias(libs.plugins.swift)
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
        val iosArm64Main by getting
        val iosX64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain.get())

            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            iosX64Main.dependsOn(this)
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
