import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 26
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        namespace = "io.github.landrynorris.autofill"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":database"))
        }
        androidMain.dependencies {
            implementation(libs.sql.android)
            implementation(libs.activity.compose)
            implementation(libs.material)
            implementation(libs.decompose)
            implementation(libs.autofill)

            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.ui)
            implementation(libs.material)
        }
    }
}
