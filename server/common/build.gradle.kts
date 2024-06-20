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

dependencies {
    compile(project(":core"))
    integration("net.labymod.serverapi.integration:voicechat:0.0.6")
    integration("net.labymod.serverapi.integration:betterperspective:0.0.6")
}
