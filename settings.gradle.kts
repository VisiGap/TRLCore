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
    val errorText = """
        
        =====================[ ERROR ]=====================
         The TRLCore project directory is not a properly cloned Git repository.
         
         In order to build TRLCore from source you must clone
         the TRLCore repository using Git, not download a code
         zip from GitHub.
         
         Built TRLCore jars are available for download at
         https://github.com/chuyuewei/TRLCore/releases
         
         See https://github.com/chuyuewei/TRLCore/blob/HEAD/CONTRIBUTING.md
         for further information on building and modifying TRLCore.
        ===================================================
    """.trimIndent()
    error(errorText)
}

rootProject.name = "trlcore"
for (name in listOf("trlcore-api", "trlcore-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}

optionalInclude("test-plugin")

fun optionalInclude(name: String, op: (ProjectDescriptor.() -> Unit)? = null) {
    val settingsFile = file("$name.settings.gradle.kts")
    if (settingsFile.exists()) {
        apply(from = settingsFile)
        findProject(":$name")?.let { op?.invoke(it) }
    } else {
        settingsFile.writeText(
            """
            // Uncomment to enable the '$name' project
            // include(":$name")

            """.trimIndent()
        )
    }
}
