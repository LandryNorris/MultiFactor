import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 23
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        namespace = "io.github.landrynorris.encryption"
    }

    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.compilations {
            if(HostManager.hostIsMac) {
                getByName("main") {
                    cinterops {
                        create("Attributes")
                    }
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.cryptography.provider.optimal)
            implementation(libs.cryptography.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    swiftPMDependencies {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        localSwiftPackage(
            directory = project.layout.projectDirectory.dir("src/swift"),
            products = listOf("Attributes")
        )
    }
}
