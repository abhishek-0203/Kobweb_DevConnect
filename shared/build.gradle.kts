import com.varabyte.kobweb.gradle.library.util.configAsKobwebLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kobweb.library)
    alias(libs.plugins.serialization.plugin)
}

group = "com.example.shared"
version = "1.0-SNAPSHOT"

kotlin {
    configAsKobwebLibrary(includeServer = true)

    js(IR) { browser() }
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.silk)
                implementation(libs.silk.icons.fa)
            }
        }

        val jvmMain by getting {
            dependencies {}
        }

        //androidMain.dependencies {}
    }
}

// Ensure KSP cache path exists before KSP tasks run (avoid "symbols (No such file or directory)")
val sharedKspJsDir = layout.buildDirectory.dir("kspCaches/js/jsMain").get().asFile
val sharedKspJvmDir = layout.buildDirectory.dir("kspCaches/jvm/jvmMain").get().asFile
val sharedKspJsSymbols = layout.buildDirectory.file("kspCaches/js/jsMain/symbols").get().asFile
val sharedKspJvmSymbols = layout.buildDirectory.file("kspCaches/jvm/jvmMain/symbols").get().asFile

if (!sharedKspJsDir.exists()) sharedKspJsDir.mkdirs()
if (!sharedKspJvmDir.exists()) sharedKspJvmDir.mkdirs()
if (!sharedKspJsSymbols.exists()) sharedKspJsSymbols.createNewFile()
if (!sharedKspJvmSymbols.exists()) sharedKspJvmSymbols.createNewFile()
if (sharedKspJsSymbols.length() == 0L) sharedKspJsSymbols.writeText("{}")
if (sharedKspJvmSymbols.length() == 0L) sharedKspJvmSymbols.writeText("{}")

tasks.matching { it.name == "kspKotlinJs" || it.name == "kspKotlinJvm" }.configureEach {
    doFirst {
        if (!sharedKspJsDir.exists()) sharedKspJsDir.mkdirs()
        if (!sharedKspJvmDir.exists()) sharedKspJvmDir.mkdirs()
        if (!sharedKspJsSymbols.exists()) sharedKspJsSymbols.createNewFile()
        if (!sharedKspJvmSymbols.exists()) sharedKspJvmSymbols.createNewFile()
        if (sharedKspJsSymbols.length() == 0L) sharedKspJsSymbols.writeText("{}")
        if (sharedKspJvmSymbols.length() == 0L) sharedKspJvmSymbols.writeText("{}")
    }
}
