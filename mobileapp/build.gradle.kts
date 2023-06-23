import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import java.util.*

val keystoreProperties =
    Properties().apply {
        val file = File("key.properties")
        if (file.exists()) load(file.reader())
    }

val decomposeVersion: String by project
val koinVersion: String by project
val sqlVersion: String by project
val settingsVersion: String by project
val appVersion: String by project

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
    id("org.jetbrains.kotlinx.kover")
    id("com.codingfeline.buildkonfig")
}

version = appVersion

kotlin {
    android()
    jvm()

    listOf(
        iosX64("uikitX64"),
        iosArm64("uikitArm64"),
        iosSimulatorArm64("uikitSimulatorArm64")
    )

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":otp"))
                implementation(project(":encryption"))
                implementation(project(":password-generator"))
                implementation(project(":database"))
                implementation("org.jetbrains.kotlinx:atomicfu:0.17.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqlVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                implementation("com.russhwolf:multiplatform-settings:$settingsVersion")
                implementation("com.russhwolf:multiplatform-settings-coroutines:$settingsVersion")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.russhwolf:multiplatform-settings-test:$settingsVersion")
                implementation("io.insert-koin:koin-test:$koinVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(project(":autofill"))
                implementation("androidx.activity:activity-compose:1.7.2")
                implementation("com.google.android.material:material:1.9.0")
                implementation("androidx.startup:startup-runtime:1.1.1")
                implementation("com.squareup.sqldelight:android-driver:$sqlVersion")
                implementation("io.insert-koin:koin-android:$koinVersion")
                implementation("com.russhwolf:multiplatform-settings-datastore:$settingsVersion")
                implementation("androidx.datastore:datastore-preferences:1.0.0")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("com.squareup.sqldelight:sqlite-driver:$sqlVersion")
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("androidx.test:core:1.5.0")
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTestJUnit4)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.squareup.sqldelight:sqlite-driver:$sqlVersion")
            }
        }

        val uikitX64Main by getting
        val uikitArm64Main by getting
        val uikitSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            uikitX64Main.dependsOn(this)
            uikitArm64Main.dependsOn(this)
            uikitSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlVersion")
            }
        }

        val uikitX64Test by getting
        val uikitArm64Test by getting
        val uikitSimulatorArm64Test by getting
        val iosTest by creating {
            uikitArm64Test.dependsOn(this)
            uikitX64Test.dependsOn(this)
            uikitSimulatorArm64Test.dependsOn(this)
        }
    }
}

kotlin {
    cocoapods {
        version = "0.0.1"
        homepage = "https://github.com/LandryNorris/MultiFactor"
        summary = "Logic for MultiFactor app"

        podfile = project.file("../iosAppXcode/Podfile")

        framework {
            baseName = "MobileApp"

            isStatic = false
            embedBitcode(BitcodeEmbeddingMode.DISABLE)

            freeCompilerArgs += listOf(
                "-linker-option", "-framework", "-linker-option", "Metal",
                "-linker-option", "-framework", "-linker-option", "CoreText",
                "-linker-option", "-framework", "-linker-option", "CoreGraphics"
            )
        }
    }
}

kotlin {
    targets.withType<KotlinNativeTarget> {
        binaries.withType<TestExecutable> {
            freeCompilerArgs += listOf("-linker-option", "-framework", "-linker-option", "Metal")
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 33

        applicationId = "io.github.landrynorris.multifactor"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    flavorDimensions += "track"

    buildFeatures {
        compose = true
    }

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
    implementation(project(mapOf("path" to ":database")))
}

kotlin {
    targets.withType<KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ComposeCodeViewer"
            packageVersion = "1.0.0"

            macOS {
                // Use -Pcompose.desktop.mac.sign=true to sign and notarize.
                bundleID = "landrynorris.MultiFactor"
            }
        }
    }
}

val buildId: String by project

buildkonfig {
    packageName = "io.github.landrynorris.mobileapp.config"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "version", version.toString())
        buildConfigField(FieldSpec.Type.STRING, "buildId", buildId)
    }
}
