pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "MultiFactor"
//include(":iosApp")
include(":mobileapp")
include(":otp")
include(":encryption")
include(":password-generator")
include(":autofill")
include(":database")
include(":sync")
