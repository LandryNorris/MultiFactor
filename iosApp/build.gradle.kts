plugins {
    id("org.jetbrains.gradle.apple.applePlugin") version "222.3345.143-0.16"
}

apple {
    iosApp {
        productName = "MultiFactor"

        sceneDelegateClass = "SceneDelegate"
        launchStoryboard = "LaunchScreen"
        mainStoryboard = "LaunchScreen"

        //productInfo["NSAppTransportSecurity"] = mapOf("NSAllowsArbitraryLoads" to true)
        //buildSettings.OTHER_LDFLAGS("")

        dependencies {
            implementation(project(":mobileapp"))
        }
    }
}
