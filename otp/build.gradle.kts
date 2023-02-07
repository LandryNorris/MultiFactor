val kryptoVersion: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlinx.kover")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("signing")
}

group = "io.github.landrynorris"
version = "0.0.1"

kotlin {
    android()
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
        val commonMain by getting {
            dependencies {
                implementation("com.soywiz.korlibs.krypto:krypto:$kryptoVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
        }
    }
}

val properties by lazy {
    Properties().also { it.load(project.rootProject.file("local.properties").inputStream()) }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        withType<MavenPublication> {
            artifact(javadocJar.get())
            pom {
                name.set("jni-utils")
                description.set("Kotlin/Native bindings for JNI on android ndk")
                url.set("https://github.com/LandryNorris/JniUtils")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                scm {
                    connection.set("https://github.com/LandryNorris/JniUtils.git")
                    developerConnection.set("https://github.com/LandryNorris/JniUtils")
                    url.set("https://github.com/LandryNorris/JniUtils")
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

project.signing {
    val secretKeyFile = getProperty("signing.secretKeyRingFile") ?: error("No key file found")
    val secretKey = File(secretKeyFile).readText()
    val signingPassword = getProperty("signing.password")
    useInMemoryPgpKeys(secretKey, signingPassword)
    sign(project.publishing.publications)
}

fun getProperty(name: String): String? {
    return System.getProperty(name) ?: properties.getProperty(name)
}
