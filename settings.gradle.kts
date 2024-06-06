rootProject.name = "labymod4-server-api"

include(":protocol")
include(":core")

sequenceOf("spigot").forEach {
    val name = "server-$it"

    include(name)
    project(":${name}").projectDir = file("server/$it")
}
