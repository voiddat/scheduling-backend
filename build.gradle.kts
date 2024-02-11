plugins {
    id("java")
    id("groovy")
}

group = "org.voiddat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.groovy:groovy-all:4.0.18")
    testImplementation("org.spockframework:spock-core:2.4-M1-groovy-4.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}