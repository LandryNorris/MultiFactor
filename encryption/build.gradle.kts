import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
}

kotlin {
    android {
        compileSdk = 37
        minSdk = 23
        namespace = "io.github.landrynorris.encryption"
        withHostTest { }
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
