import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    id("org.jetbrains.kotlinx.kover")
    alias(libs.plugins.dokka)
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 23
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        namespace = "io.github.landrynorris.password.generator"
    }
    jvm()

    listOf(iosArm64(), iosSimulatorArm64())

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
