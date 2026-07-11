plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ktfmt)
}

kotlin {
    android {
        compileSdk = 37
        minSdk = 26
        namespace = "io.github.landrynorris.autofill"
        withHostTest { }
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
