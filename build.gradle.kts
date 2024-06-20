plugins {
    id("java")
    id("maven-publish")
}

group = "net.labymod.serverapi"
version = System.getenv("VERSION") ?: "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

val commonOutputDir = "${buildDir}/commonOutput"

tasks.register<Delete>("cleanCommonOutput") {
    delete(commonOutputDir)
}

tasks.named("clean") {
    dependsOn("cleanCommonOutput")
}

tasks.named("build") {
    dependsOn("cleanCommonOutput")
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("maven-publish")

    group = rootProject.group
    version = rootProject.version

    val compile = configurations.create("compile")
    val api by configurations
    api.extendsFrom(compile)

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://dist.labymod.net/api/v1/maven/release/")
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:22.0.0")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    fun adjustArchiveFileName(property: Property<String>) {
        var value = property.get()
        if (name != "server-common") {
            value = value.replace("server-", "")
        }

        property.set("labymod-server-api-$value")
    }

    tasks.jar {
        adjustArchiveFileName(archiveFileName)

        fun includeProjectDependencies(config: Configuration, visited: MutableSet<Project>) {
            config.dependencies.forEach { dependency ->
                if (dependency is ProjectDependency) {
                    val project = dependency.dependencyProject
                    if (visited.add(project)) {
                        from(project.sourceSets["main"].output)
                        includeProjectDependencies(project.configurations["compile"], visited)
                    }
                }
            }
        }

        if (System.getenv("DEFAULT_BUILD") != "true") {
            includeProjectDependencies(configurations["compile"], mutableSetOf())
        }
    }

    tasks.register("sourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        adjustArchiveFileName(archiveFileName)

        from(sourceSets.main.get().allSource)
    }

    var publishToken = System.getenv("PUBLISH_TOKEN")

    if (publishToken == null && project.hasProperty("net.labymod.distributor.publish-token")) {
        publishToken = project.property("net.labymod.distributor.publish-token").toString()
    }

    java {
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                from(components["java"])
            }
        }

        repositories {
            maven(project.property("net.labymod.distributor-url").toString()) {
                name = "LabyMod-Distributor"

                authentication {
                    create<HttpHeaderAuthentication>("header")
                }

                credentials(HttpHeaderCredentials::class) {
                    name = "Publish-Token"
                    value = publishToken
                }
            }
        }
    }

    tasks.register<Copy>("copyToCommonOutput") {
        val commonOutputDir = project.rootProject.buildDir.resolve("commonOutput")

        // Copy regular JAR files
        val shadowTask = tasks.findByName("shadowJar")

        var buildTask = if (shadowTask != null) "shadowJar" else "jar"
        from(tasks.named(buildTask).map { it.outputs.files })

        // Copy sources JAR files if they exist
        val sourcesJarTask = tasks.findByName("sourcesJar")
        if (sourcesJarTask != null) {
            from(sourcesJarTask.outputs.files)
        }

        into(commonOutputDir)
    }

    tasks.named("build") {
        dependsOn("copyToCommonOutput")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}