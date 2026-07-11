import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.*

val appVersion = project.property("appVersion") as String

plugins {
    alias(libs.plugins.kotlin)
    kotlin("native.cocoapods")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sql.delight)
    alias(libs.plugins.kover)
    alias(libs.plugins.buildkonfig)
}

version = appVersion

kotlin {
    android {
        compileSdk = 37
        minSdk = 21
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        namespace = "io.github.landrynorris.app"
        withHostTest { }
    }
    jvm()

    listOf(
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
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.ui)
            implementation(libs.material3)
            implementation(libs.material.icons.extended)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.settings.test)
            implementation(libs.koin.test)
        }
        androidMain.dependencies {
            implementation(project(":autofill"))
            implementation(project(":database"))
            implementation(libs.activity.compose)
            implementation(libs.material)
            implementation(libs.startup.runtime)
            implementation(libs.sql.android)
            implementation(libs.koin.android)
            implementation(libs.settings.datastore)
            implementation(libs.datastore.preferences)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.sql.sqlite)
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

val buildId = project.property("buildId") as String

buildkonfig {
    packageName = "io.github.landrynorris.mobileapp.config"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "version", version.toString())
        buildConfigField(FieldSpec.Type.STRING, "buildId", buildId)
    }
}
