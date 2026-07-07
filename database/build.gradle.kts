import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.sql.delight)
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 23
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        namespace = "io.github.landrynorris.database"
    }
    jvm()

    listOf(iosArm64(), iosSimulatorArm64())

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.sql)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosArm64Test.dependsOn(this)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("io.github.landrynorris.database")
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}
