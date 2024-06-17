plugins {
    id("java")
}

dependencies {
    api(project(":core"))
    implementation("com.google.auto.value:auto-value:1.11.0")
    annotationProcessor("com.google.auto.value:auto-value:1.11.0")
}
