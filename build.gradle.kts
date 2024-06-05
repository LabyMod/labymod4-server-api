plugins {
    id("java")
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
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:22.0.0")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.jar {
        archiveFileName.set("labymod-server-api-" + archiveFileName.get().replace("server-", ""))

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

        includeProjectDependencies(configurations["compile"], mutableSetOf())
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}