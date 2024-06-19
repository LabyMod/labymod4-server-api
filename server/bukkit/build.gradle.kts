plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    // this is the oldest available version of LabyMod 4. As of 1.20.6 nothing broke, so this should be fine.
    compileOnly("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
    api(project(":server-common"))
}

tasks.processResources {
    inputs.property("version", version)
    outputs.upToDateWhen { false }
    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}

fun adjustArchiveFileName(property: Property<String>) {
    var value = property.get()
    if (name != "server-common") {
        value = value.replace("server-", "")
    }

    property.set("labymod-server-api-$value")
}

tasks.shadowJar {
    dependsOn(":server-common:shadowJar")
    adjustArchiveFileName(archiveBaseName)
    archiveClassifier.set("")
}

tasks.jar {
    finalizedBy("shadowJar")
}
