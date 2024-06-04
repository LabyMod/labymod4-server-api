rootProject.name = "labymod4-server-api"

include(":protocol")
include(":core")
include(":server")

sequenceOf("bukkit").forEach {
    val name = "server-$it"

    include(name)
    project(":${name}").projectDir = file("server/$it")
}
