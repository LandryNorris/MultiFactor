import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.gradle.kotlin.dsl.implementation
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
    alias(libs.plugins.kotlin)
    kotlin("native.cocoapods")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.app)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sql.delight)
    alias(libs.plugins.kover)
    alias(libs.plugins.buildkonfig)
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
            implementation(libs.decompose)
            implementation(libs.koin)
            implementation(libs.kotlin.coroutines)
            implementation(libs.sql)
            implementation(libs.decompose.compose)
            implementation(libs.settings)
            implementation(libs.settings.coroutines)
            implementation(libs.color)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.settings.test)
            implementation(libs.koin.test)
        }
        androidMain.dependencies {
            implementation(project(":autofill"))
            implementation(libs.activity.compose)
            implementation(libs.material)
            implementation(libs.startup.runtime)
            implementation(libs.android.driver)
            implementation(libs.koin.android)
            implementation(libs.settings.datastore)
            implementation(libs.datastore.preferences)
        }
        androidUnitTest.dependencies {
            implementation(libs.sql.sqlite)
        }
        androidInstrumentedTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.core)
            implementation(compose.desktop.uiTestJUnit4)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.sql.sqlite)
            implementation(libs.kotlinx.coroutines.swing)
        }

        iosMain.dependencies {
            implementation(libs.sql.native)
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
