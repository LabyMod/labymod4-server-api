import org.gradle.api.tasks.SourceSetContainer

val licenseHeaderFile = rootProject.file("LICENSE")
val licenseHeader = licenseHeaderFile
    .readLines()
    .joinToString(separator = "\n", prefix = "/*\n", postfix = "\n */\n\n") {
        if (it.isEmpty()) " *" else " * $it"
    }
val mainSourceSet = extensions.getByType<SourceSetContainer>().named("main")

tasks.register("checkLicenseMain") {
    val javaSources = mainSourceSet.map { it.allJava }
    inputs.file(licenseHeaderFile)
    inputs.files(javaSources)

    doLast {
        val missingLicense = javaSources.get().files.filter { file ->
            file.isFile && !file.readText().startsWith(licenseHeader)
        }

        if (missingLicense.isNotEmpty()) {
            throw GradleException(
                "Missing or outdated license headers:\n" +
                    missingLicense.joinToString("\n") { it.relativeTo(projectDir).path }
            )
        }
    }
}

tasks.register("updateLicenseMain") {
    val javaSources = mainSourceSet.map { it.allJava }
    inputs.file(licenseHeaderFile)
    inputs.files(javaSources)
    outputs.upToDateWhen { false }

    doLast {
        javaSources.get().files.filter { it.isFile }.forEach { file ->
            val content = file.readText()
            val updatedContent = when {
                content.startsWith(licenseHeader) -> content
                content.startsWith("/*") && content.substringBefore("*/").contains("MIT License") ->
                    licenseHeader + content.substringAfter("*/").trimStart()
                else -> licenseHeader + content
            }

            if (updatedContent != content) {
                file.writeText(updatedContent)
            }
        }
    }
}

tasks.register("updateLicenses") {
    dependsOn("updateLicenseMain")
}
