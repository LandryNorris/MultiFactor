plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

val sqlVersion: String by project
val decomposeVersion: String by project

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":database"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlVersion")
                implementation("androidx.activity:activity-compose:1.5.1")
                implementation("com.google.android.material:material:1.6.1")
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("androidx.autofill:autofill:1.1.0")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material)
            }
        }
        val androidTest by getting
    }
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }
    namespace = "io.github.landrynorris.autofill"
}