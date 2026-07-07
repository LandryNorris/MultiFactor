pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
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
