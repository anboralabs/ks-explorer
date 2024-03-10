plugins {
    id("java")
}

group = "co.anbora.labs.core.kse"
version = "2024.2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.bouncycastle:bcpkix-jdk18on:1.77")
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("commons-io:commons-io:2.15.1")
    implementation("com.miglayout:miglayout-swing:11.3")
    implementation("com.nimbusds:nimbus-jose-jwt:9.25.6")
}

tasks.test {
    useJUnitPlatform()
}