/**
 * TRLCore Build Utilities
 * Contains helper tasks for build optimization
 */

// ===== Build Speed Tasks =====

tasks.register("cleanBuildCache") {
    group = "build optimization"
    description = "Cleans local and remote build caches"
    doLast {
        delete(layout.projectDirectory.dir(".gradle/caches"))
        delete(layout.projectDirectory.dir(".gradle/build-cache"))
        println("Build caches cleaned!")
    }
}

tasks.register("showBuildInfo") {
    group = "build optimization"
    description = "Shows current build configuration and JVM info"
    doLast {
        println("=== TRLCore Build Information ===")
        println("Gradle Version: ${gradle.gradleVersion}")
        println("Java Version: ${System.getProperty("java.version")}")
        println("Java Vendor: ${System.getProperty("java.vendor")}")
        println("JVM Max Memory: ${Runtime.getRuntime().maxMemory() / 1024 / 1024}MB")
        println("Available Processors: ${Runtime.getRuntime().availableProcessors()}")
        println("OS: ${System.getProperty("os.name")} ${System.getProperty("os.arch")}")
        println("")
        println("Build Settings:")
        println("  Configuration Cache: ${gradle.startParameter.isConfigurationCacheRequested}")
        println("  Parallel Build: ${gradle.startParameter.isParallelProjectExecutionEnabled}")
        println("  Build Cache: ${gradle.startParameter.isBuildCacheEnabled}")
        println("  Max Workers: ${gradle.startParameter.maxWorkerCount}")
    }
}

tasks.register("optimizedBuild") {
    group = "build optimization"
    description = "Runs optimized build with all performance flags"
    dependsOn("build")
    doFirst {
        println("Running optimized build...")
    }
}

// ===== Dependency Tasks =====

tasks.register("listDependencies") {
    group = "build optimization"
    description = "Lists all project dependencies"
    doLast {
        println("=== Project Dependencies ===")
        configurations.filter { it.isCanBeResolved }.forEach { conf ->
            println("\n[${conf.name}]")
            conf.resolvedConfiguration.lenientConfiguration.allModuleDependencies.forEach { dep ->
                println("  ${dep.moduleGroup}:${dep.name}:${dep.moduleVersion}")
            }
        }
    }
}

tasks.register("checkDependencyUpdates") {
    group = "build optimization"
    description = "Placeholder for dependency update check"
    doLast {
        println("Run './gradlew dependencyUpdates' with version catalog plugin for full update check")
    }
}

// ===== Report Tasks =====

tasks.register("buildReport") {
    group = "build optimization"
    description = "Generates a build performance report"
    doLast {
        val reportFile = layout.buildDirectory.file("reports/build-report.txt").get().asFile
        reportFile.parentFile.mkdirs()
        reportFile.writeText(buildString {
            appendLine("=== TRLCore Build Report ===")
            appendLine("Generated: ${java.time.LocalDateTime.now()}")
            appendLine("")
            appendLine("Project: ${project.name}")
            appendLine("Version: ${project.version}")
            appendLine("Gradle: ${gradle.gradleVersion}")
            appendLine("")
            appendLine("Build Settings:")
            appendLine("  Parallel: ${gradle.startParameter.isParallelProjectExecutionEnabled}")
            appendLine("  Workers: ${gradle.startParameter.maxWorkerCount}")
            appendLine("  Cache: ${gradle.startParameter.isBuildCacheEnabled}")
        })
        println("Build report saved to: ${reportFile.absolutePath}")
    }
}
