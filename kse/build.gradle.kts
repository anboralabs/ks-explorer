plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

group = "co.anbora.labs.core.kse"
version = "2024.3.1"

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
    implementation("org.bouncycastle:bcpkix-jdk18on:1.83")
    implementation("net.java.dev.jna:jna:5.18.1")
    implementation("commons-io:commons-io:2.21.0")
    implementation("com.miglayout:miglayout-swing:11.4.2")
    implementation("com.nimbusds:nimbus-jose-jwt:10.7")
}

tasks.test {
    useJUnitPlatform()
}
