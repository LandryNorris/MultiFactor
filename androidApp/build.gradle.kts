import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
}

val keystoreProperties =
    Properties().apply {
        val file = File("key.properties")
        if (file.exists()) load(file.reader())
    }

val appVersion = project.property("appVersion") as String

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = 36
    defaultConfig {
        minSdk = 26
        targetSdk = 36

        applicationId = "io.github.landrynorris.multifactor"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    flavorDimensions += "track"

    productFlavors {
        val production by creating {
            if(keystoreProperties.isNotEmpty()) {
                signingConfigs {
                    create("release") {
                        storeFile = file(keystoreProperties.getProperty("storeFile"))
                        storePassword = keystoreProperties.getProperty("storePassword")
                        keyAlias = keystoreProperties.getProperty("keyAlias")
                        keyPassword = keystoreProperties.getProperty("keyPassword")
                    }
                }
                signingConfig = signingConfigs.getByName("release")
            }
        }

        val dev by creating {
            applicationIdSuffix = ".dev"
        }
    }

    namespace = "io.github.landrynorris.multifactor"
}

dependencies {
    implementation(project(":app"))
    implementation(project(":autofill"))
    implementation(project(":encryption"))
    implementation(libs.startup.runtime)
    implementation(libs.koin)
    implementation(libs.koin.android)
    implementation(libs.decompose.compose)
    implementation(libs.decompose)

    implementation(libs.activity.compose)
}
