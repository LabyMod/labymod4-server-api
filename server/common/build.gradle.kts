plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

dependencies {
    compile(project(":core"))
    api("net.labymod.serverapi.integration:voicechat:0.0.4") {
        exclude("net.labymod.serverapi")
    }
    //api("net.labymod.serverapi.integration:togglesneak:0.0.0") {
    //    exclude("net.labymod.serverapi")
    //}
}

fun adjustArchiveFileName(property: Property<String>) {
    var value = property.get()
    if (name != "server-common") {
        value = value.replace("server-", "")
    }

    property.set("labymod-server-api-$value")
}

tasks.shadowJar {
    adjustArchiveFileName(archiveBaseName)
    archiveClassifier.set("")
    mergeServiceFiles()
}

tasks.jar {
    if (System.getenv("DEFAULT_BUILD") != "true") {
        finalizedBy("shadowJar")
    }
}