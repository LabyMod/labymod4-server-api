rootProject.name = "labymod4-server-api"

include(":api")
include(":core")

sequenceOf("common", "spigot").forEach {
    val name = "server-$it"

    include(name)
    project(":${name}").projectDir = file("server/$it")
}
