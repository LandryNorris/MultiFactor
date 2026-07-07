import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
    id("maven-publish")
    id("signing")
}

group = "io.github.landrynorris"
version = "0.1.0"

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 21
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        withSourcesJar()

        namespace = "io.github.landrynorris.otp"
    }
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.cryptography.provider.optimal)
            implementation(libs.cryptography.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

val properties by lazy {
    Properties().also { it.load(project.rootProject.file("local.properties").inputStream()) }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

if(hasLocalProperties()) {
    publishing {
        publications {
            withType<MavenPublication> {
                artifact(javadocJar.get())
                pom {
                    name.set("otp")
                    description.set("OTP implementation for Kotlin Multiplatform")
                    url.set("https://github.com/LandryNorris/MultiFactor")
                    licenses {
                        license {
                            name.set("Apache 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }
                    scm {
                        connection.set("https://github.com/LandryNorris/MultiFactor.git")
                        developerConnection.set("https://github.com/LandryNorris/MultiFactor")
                        url.set("https://github.com/LandryNorris/MultiFactor")
                    }
                    developers {
                        developer {
                            id.set("landrynorris")
                            name.set("Landry Norris")
                            email.set("landry.norris0@gmail.com")
                        }
                    }
                }
            }
        }

        repositories {
            maven {
                name = "sonatype"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

                credentials {
                    username = getProperty("sonatype.username")
                    password = getProperty("sonatype.password")
                }
            }
        }
    }
}

if(hasLocalProperties()) {
    project.signing {
        val secretKeyFile = getProperty("signing.secretKeyRingFile") ?: error("No key file found")
        val secretKey = File(secretKeyFile).readText()
        val signingPassword = getProperty("signing.password")
        useInMemoryPgpKeys(secretKey, signingPassword)
        sign(project.publishing.publications)
    }
}

fun getProperty(name: String): String? {
    return System.getProperty(name) ?: properties.getProperty(name)
}

fun hasLocalProperties(): Boolean {
    return project.rootProject.file("local.properties").exists() &&
            getProperty("signing.enable") != "false"
}
