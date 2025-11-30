import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Temporarily disabled - TRLCore is in a subdirectory of TRL-Plugin Git repo
/*
if (!file(".git").exists()) {
    // TRLCore-Finally - based on Leaf - project setup
    val errorText = """
        
        =====================[ ERROR ]=====================
         The TRLCore-Finally project directory is not a properly cloned Git repository.
         
         In order to build TRLCore-Finally from source you must clone
         the repository using Git, not download a code zip from GitHub.
         
         TRLCore-Finally is based on Leaf. For original Leaf builds:
         https://www.leafmc.one/download
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper forks.
        ===================================================
    """.trimIndent()
    // TRLCore-Finally end - project setup
    error(errorText)
}
*/

rootProject.name = "trlcore-finally"

// Note: Still using "leaf-api" and "leaf-server" module names for compatibility
for (name in listOf("leaf-api", "leaf-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
}
