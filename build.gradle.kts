import com.sun.tools.javac.Main.compile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("com.google.protobuf:protobuf-java:3.4.0")
    runtimeOnly("com.google.protobuf:protobuf-java:3.4.0")
    compileOnly(group="org.apache.pulsar", name="pulsar-client-all", version="2.8.1")
    runtimeOnly(group="org.apache.pulsar", name="pulsar-client-all", version="2.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

application {
    mainClass.set("MainKt")
}