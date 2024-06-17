plugins {
    id("java")
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    // this is the oldest available version of LabyMod 4. As of 1.20.6 nothing broke, so this should be fine.
    compileOnly("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
    compile(project(":server-common"))
}

tasks.processResources {
    inputs.property("version", version)
    outputs.upToDateWhen { false }
    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}
