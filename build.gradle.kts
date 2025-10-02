plugins {
    // Top-level Gradle plugin configuration for the Mandel project.
    // By using 'apply false', we make these plugins available to all subprojects,
    // but avoid applying them here, as they should only be applied where needed.
    
    // Android application plugin for building APKs
    alias(libs.plugins.androidApplication) apply false
    // Android library plugin for building shared Android libraries
    alias(libs.plugins.androidLibrary) apply false
    // Compose Multiplatform plugin for sharing UI code across platforms
    alias(libs.plugins.composeMultiplatform) apply false
    // Compose compiler plugin (required for Compose to work)
    alias(libs.plugins.composeCompiler) apply false
    // Kotlin Multiplatform plugin to enable cross-platform code sharing
    alias(libs.plugins.kotlinMultiplatform) apply false
}