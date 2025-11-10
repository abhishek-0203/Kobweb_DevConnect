import java.io.File

// Ensure site KSP cache symbols exist early during configuration to avoid KSP FileNotFound errors
val siteKspJsSymbols = File(rootDir, "site/build/kspCaches/js/jsMain/symbols")
val siteKspJvmSymbols = File(rootDir, "site/build/kspCaches/jvm/jvmMain/symbols")

fun ensureSymbols(file: File) {
    try {
        val dir = file.parentFile
        if (!dir.exists()) dir.mkdirs()
        if (!file.exists()) file.createNewFile()
        if (file.length() == 0L) file.writeText("{}")
    } catch (e: Exception) {
        // Best-effort; avoid failing configuration. KSP tasks will also attempt to create files.
        println("Warning: failed to create KSP symbols file ${file.path}: ${e.message}")
    }
}

ensureSymbols(siteKspJsSymbols)
ensureSymbols(siteKspJvmSymbols)

/*
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
}
*/

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
   // alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kobweb.library) apply false
    alias(libs.plugins.serialization.plugin) apply false
    alias(libs.plugins.kobweb.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    //alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    //alias(libs.plugins.mongodb.realm) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
