plugins {
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("org.jetbrains.dokka") version "1.7.20"
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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlVersion")
        classpath("com.android.tools.build:gradle:7.4.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

koverMerged {
    enable()

    filters {
        classes {
            excludes += listOf("*.BuildConfig", "*.MainActivity*", "*.compose.*", "*.theme.*", "*.platform.*")
        }

        projects {
            excludes += listOf("database", "autofill", "encryption")
        }
    }

    htmlReport {
        onCheck.set(true)
        reportDir.set(File(buildDir, "test/report/html"))
    }
}
