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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlVersion")
        classpath("com.android.tools.build:gradle:7.3.0-rc01")
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