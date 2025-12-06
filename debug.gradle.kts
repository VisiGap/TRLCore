
tasks.register("inspectPaperweight") {
    doLast {
        val paper = paperweight.upstreams.getByName("paper")
        println("Class: " + paper::class.qualifiedName)
        // print all methods/properties if possible via reflection?
        paper::class.members.forEach { println(it.name) }
    }
}
