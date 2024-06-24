plugins {
    id("java")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
}

var generateJavaTask = tasks.create("generateJava", Copy::class) {
    val properties = mapOf("version" to project.version.toString())
    inputs.properties(properties)

    from("src/template/java")
    into("${project.layout.buildDirectory.get().asFile}/generated/java")

    expand(properties)
}


sourceSets.findByName("main")?.apply {
    this.java.srcDirs("${project.buildDir}/generated/java")
}

tasks.named("checkLicenseMain") {
    dependsOn(generateJavaTask)
}

tasks.named("updateLicenseMain") {
    dependsOn(generateJavaTask)
}

tasks.named("sourcesJar") {
    dependsOn(generateJavaTask)
}

tasks.compileJava {
    dependsOn(generateJavaTask)
}
