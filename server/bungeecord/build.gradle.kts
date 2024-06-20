plugins {
    id("java")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // this is the oldest available version of LabyMod 4. As of 1.20.6 nothing broke, so this should be fine.
    compileOnly("net.md-5:bungeecord-api:1.8-SNAPSHOT")
}

tasks.processResources {
    inputs.property("version", version)
    outputs.upToDateWhen { false }
    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}
