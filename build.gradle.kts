plugins {
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
    id("org.jetbrains.kotlinx.kover") version "0.9.3"
    id("org.jetbrains.dokka") version "2.1.0"
}

buildscript {
    val composeVersion: String by project
    val sqlVersion: String by project
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.21")
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("app.cash.sqldelight:gradle-plugin:$sqlVersion")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")
        classpath("com.android.tools.build:gradle:8.11.0")
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

koverReport {
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

    defaults {
        html {
            onCheck = true

            setReportDir(layout.buildDirectory.dir("test/report/html"))
        }
    }
}

tasks {
    dokkaHtmlMultiModule {
        outputDirectory.set(File(projectDir, "docs/html"))
    }
}
