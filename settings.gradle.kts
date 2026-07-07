pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "MultiFactor"
//include(":iosApp")
include(":app")
include(":otp")
include(":encryption")
include(":password-generator")
include(":autofill")
include(":database")

include(":androidApp")
