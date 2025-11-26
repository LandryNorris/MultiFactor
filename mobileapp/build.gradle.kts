import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import java.util.*

val keystoreProperties =
    Properties().apply {
        val file = File("key.properties")
        if (file.exists()) load(file.reader())
    }

val decomposeVersion: String by project
val koinVersion: String by project
val sqlVersion: String by project
val settingsVersion: String by project
val appVersion: String by project

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization") version "1.9.21"
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("app.cash.sqldelight")
    id("org.jetbrains.kotlinx.kover")
    id("com.codingfeline.buildkonfig")
}

version = appVersion

kotlin {
    androidTarget()
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain.dependencies {
            implementation(project(":otp"))
            implementation(project(":encryption"))
            implementation(project(":password-generator"))
            implementation(project(":database"))
            implementation("org.jetbrains.kotlinx:atomicfu:0.21.0")
            implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
            implementation("io.insert-koin:koin-core:$koinVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("app.cash.sqldelight:coroutines-extensions:$sqlVersion")
            implementation("com.arkivanov.decompose:extensions-compose:$decomposeVersion")
            implementation("com.russhwolf:multiplatform-settings:$settingsVersion")
            implementation("com.russhwolf:multiplatform-settings-coroutines:$settingsVersion")
            implementation("com.materialkolor:material-kolor:1.2.8")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("com.russhwolf:multiplatform-settings-test:$settingsVersion")
            implementation("io.insert-koin:koin-test:$koinVersion")
        }
        androidMain.dependencies {
            implementation(project(":autofill"))
            implementation("androidx.activity:activity-compose:1.11.0")
            implementation("com.google.android.material:material:1.13.0")
            implementation("androidx.startup:startup-runtime:1.2.0")
            implementation("app.cash.sqldelight:android-driver:$sqlVersion")
            implementation("io.insert-koin:koin-android:$koinVersion")
            implementation("com.russhwolf:multiplatform-settings-datastore:$settingsVersion")
            implementation("androidx.datastore:datastore-preferences:1.1.7")
        }
        androidUnitTest.dependencies {
            implementation("app.cash.sqldelight:sqlite-driver:$sqlVersion")
        }
        androidInstrumentedTest.dependencies {
            implementation(kotlin("test"))
            implementation("androidx.test:core:1.7.0")
            implementation(compose.desktop.uiTestJUnit4)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("app.cash.sqldelight:sqlite-driver:$sqlVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")
        }

        iosMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:${sqlVersion}")
        }
    }
}

kotlin {
    cocoapods {
        version = "0.0.1"
        homepage = "https://github.com/LandryNorris/MultiFactor"
        summary = "Logic for MultiFactor app"

        podfile = project.file("../iosAppXcode/Podfile")

        framework {
            baseName = "MobileApp"
        }
    }
}

android {
    compileSdk = 36
    defaultConfig {
        minSdk = 26
        targetSdk = 36

        applicationId = "io.github.landrynorris.multifactor"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    flavorDimensions += "track"

    productFlavors {
        val production by creating {
            if(keystoreProperties.isNotEmpty()) {
                signingConfigs {
                    create("release") {
                        storeFile = file(keystoreProperties.getProperty("storeFile"))
                        storePassword = keystoreProperties.getProperty("storePassword")
                        keyAlias = keystoreProperties.getProperty("keyAlias")
                        keyPassword = keystoreProperties.getProperty("keyPassword")
                    }
                }
                signingConfig = signingConfigs.getByName("release")
            }
        }

        val dev by creating {
            applicationIdSuffix = ".dev"
        }
    }

    namespace = "io.github.landrynorris.multifactor"
}

dependencies {
    implementation(project(mapOf("path" to ":database")))
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MultiFactor"
            packageVersion = "1.0.0"

            macOS {
                // Use -Pcompose.desktop.mac.sign=true to sign and notarize.
                bundleID = "landrynorris.MultiFactor"
            }
        }
    }
}

val buildId: String by project

buildkonfig {
    packageName = "io.github.landrynorris.mobileapp.config"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "version", version.toString())
        buildConfigField(FieldSpec.Type.STRING, "buildId", buildId)
    }
}
