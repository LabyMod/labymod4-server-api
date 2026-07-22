plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
}

val generatedJavaDir = layout.buildDirectory.dir("generated/java")

val generateJavaTask = tasks.register<Copy>("generateJava") {
    val properties = mapOf("version" to project.version.toString())
    inputs.properties(properties)

    from("src/template/java")
    into(generatedJavaDir)

    expand(properties)
}


sourceSets.findByName("main")?.apply {
    this.java.srcDir(generatedJavaDir)
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
