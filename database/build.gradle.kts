plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.sql.delight)
    alias(libs.plugins.ktfmt)
}

kotlin {
    android {
        compileSdk = 37
        minSdk = 23
        namespace = "io.github.landrynorris.database"
        withHostTest {}
    }
    jvm()

    listOf(iosArm64(), iosSimulatorArm64())

    sourceSets {
        commonMain { dependencies { implementation(libs.sql) } }
        commonTest { dependencies { implementation(kotlin("test")) } }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("io.github.landrynorris.database")
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}
