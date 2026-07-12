plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktfmt)
}

kotlin {
    android {
        compileSdk = 37
        minSdk = 23
        namespace = "io.github.landrynorris.password.generator"
        withHostTest {}
    }
    jvm()

    listOf(iosArm64(), iosSimulatorArm64())

    sourceSets { commonTest.dependencies { implementation(kotlin("test")) } }
}
