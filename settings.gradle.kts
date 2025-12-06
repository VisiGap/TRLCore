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

if (!file(".git").exists()) {
    // TRLCore-Finally start - project setup
    val errorText = """
        
        =====================[ ERROR ]=====================
         The TRLCore-Finally project directory is not a properly cloned Git repository.
         
         In order to build TRLCore-Finally from source you must clone
         the TRLCore-Finally repository using Git, not download a code
         zip from GitHub.
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper forks.
        ===================================================
    """.trimIndent()
    // TRLCore-Finally end - project setup
    error(errorText)
}

rootProject.name = "trlcore-finally"

for (name in listOf("trlcore-finally-api", "trlcore-finally-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
}
