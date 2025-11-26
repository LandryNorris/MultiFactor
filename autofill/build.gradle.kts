plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val sqlVersion: String by project
val decomposeVersion: String by project

kotlin {
    androidTarget()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":database"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation("app.cash.sqldelight:android-driver:$sqlVersion")
            implementation("androidx.activity:activity-compose:1.7.2")
            implementation("com.google.android.material:material:1.9.0")
            implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
            implementation("androidx.autofill:autofill:1.1.0")

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material)
        }
    }
}

android {
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "io.github.landrynorris.autofill"
}
