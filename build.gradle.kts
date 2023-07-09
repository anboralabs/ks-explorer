plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "co.anbora.labs"
version = "2023.1.6"

repositories {
    mavenCentral()
}

configurations {
    all {
        // Allows using project dependencies instead of IDE dependencies during compilation and test running
        resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEPENDENCY_FIRST)
    }
}

dependencies {
    implementation("org.bouncycastle:bcpkix-jdk18on:1.72")
    implementation("com.nimbusds:nimbus-jose-jwt:9.25.6")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    //version.set("LATEST-EAP-SNAPSHOT")
    version.set("2022.2.5")
    type.set("IU") // Target IDE Platform
    plugins.set(listOf())
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
        changeNotes.set(file("src/main/html/change-notes.html").inputStream().readBytes().toString(Charsets.UTF_8))
        pluginDescription.set(file("src/main/html/description.html").inputStream().readBytes().toString(Charsets.UTF_8))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
