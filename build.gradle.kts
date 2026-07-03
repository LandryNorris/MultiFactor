import org.gradle.kotlin.dsl.kover

plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
}

buildscript {
    val sqlVersion: String by project
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencies {
    kover(project(":otp"))
    kover(project(":mobileapp"))
    kover(project(":password-generator"))
}

kover {
    reports {
        filters {
            excludes {
                classes("*.BuildConfig", "*.MainActivity*", "*.compose.*",
                    "*.theme.*", "*.platform.*", "*.InitializeKt*", "*.Startup*", "*.*Defaults",
                    "*.test.*", "MainKt*")

                packages("io.github.landrynorris.database", "io.github.landrynorris.autofill")

                annotatedBy(
                    "io.github.landrynorris.multifactor.annotations.IgnoreCoverage",
                    "io.github.landrynorris.otp.IgnoreCoverage"
                )
            }
        }

        total {
            html {
                onCheck = true
                htmlDir = layout.buildDirectory.dir("test/report/html")
            }
        }
    }
}

dokka {
    dokkaPublications.html {
        this.outputDirectory = project.projectDir.resolve("docs/html")
    }
}
