plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
}

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
            implementation(libs.sql.android)
            implementation(libs.activity.compose)
            implementation(libs.material)
            implementation(libs.decompose)
            implementation(libs.autofill)

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
