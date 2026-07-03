
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
    id("org.jetbrains.kotlinx.kover")
    alias(libs.plugins.dokka)
}

kotlin {
    androidTarget()
    jvm()

    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    sourceSets {
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
    namespace = "io.github.landrynorris.password.generator"
}
