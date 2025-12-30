import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "com.restaurant"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        add("implementation", rootProject.libs.kotlin.stdlib)

        // Kotest dependencies
        add("testImplementation", rootProject.libs.kotest.runner.junit5)
        add("testImplementation", rootProject.libs.kotest.assertions.core)
        add("testImplementation", rootProject.libs.kotest.property)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "22"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(22)
    }
}