plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

val sqlVersion: String by project

kotlin {
    android()
    jvm()

    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqlVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 23
        targetSdk = 33
    }
    namespace = "io.github.landrynorris.database"
}

sqldelight {
    database("AppDatabase") {
        packageName = "io.github.landrynorris.database"
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}
