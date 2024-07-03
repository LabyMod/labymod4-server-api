plugins {
    id("java")
}

val integration = configurations.create("integration") {
    withDependencies {
        dependencies.forEach { dependency ->
            if (dependency is ModuleDependency) {
                dependency.exclude(mapOf("group" to "net.labymod.serverapi"))
            }
        }
    }
}

val api by configurations
api.extendsFrom(integration)

val integrationVersion = "1.0.0"

dependencies {
    compile(project(":core"))
    integration("net.labymod.serverapi.integration:voicechat:$integrationVersion")
    integration("net.labymod.serverapi.integration:betterperspective:$integrationVersion")
}
