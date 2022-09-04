import org.jetbrains.compose.experimental.dsl.IOSDevices
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode

val decomposeVersion: String by project
val koinVersion: String by project
val sqlVersion: String by project
val settingsVersion: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    
    listOf(
        iosX64("uikitX64"),
        iosArm64("uikitArm64")
    ).forEach {
        it.binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":otp"))
                implementation(project(":encryption"))
                implementation(project(":password-generator"))
                implementation("org.jetbrains.kotlinx:atomicfu:0.17.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqlVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                implementation("com.russhwolf:multiplatform-settings-coroutines:$settingsVersion")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation("androidx.activity:activity-compose:1.5.1")
                implementation("com.google.android.material:material:1.6.1")
                implementation("androidx.startup:startup-runtime:1.1.1")
                implementation("com.squareup.sqldelight:android-driver:$sqlVersion")
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("com.russhwolf:multiplatform-settings-datastore:$settingsVersion")
                implementation("androidx.datastore:datastore-preferences:1.0.0")
            }
        }
        val androidTest by getting

        val uikitX64Main by getting
        val uikitArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            uikitX64Main.dependsOn(this)
            uikitArm64Main.dependsOn(this)

            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlVersion")
            }
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 32

        applicationId = "io.github.landrynorris.multifactor"
    }

    buildFeatures {
        compose = true
    }

    productFlavors {
        val production by creating

        val dev by creating {
            applicationIdSuffix = ".dev"
        }
    }
    namespace = "io.github.landrynorris.multifactor"
}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "io.github.landrynorris.multifactor"
        verifyMigrations = true
    }
}
