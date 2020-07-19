import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.example"
version = "1.0-SNAPSHOT"
val manifestFile = "src/main/META-INF/Manifest.MF"

plugins {
    kotlin("jvm") version "1.3.72"
    id("org.jmailen.kotlinter") version "2.4.1"
    id("application")
}

application {
    mainClassName = "org.example.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}

tasks.withType<KotlinCompile>().all {
   kotlinOptions {
       jvmTarget = "1.8"
   }
}


tasks.jar {
    doFirst {

    }

    manifest {
        from(manifestFile)
    }
}

tasks.register<Jar>("fullJar") {
    archiveClassifier.set("full")

    manifest {
        from(manifestFile)
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.getByName("build").dependsOn(tasks.getByName("fullJar"))