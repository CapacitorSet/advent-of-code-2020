import java.io.File

// The final newline is dropped.
val groups = File("input").readText().dropLast(1).split(Regex("\n\n"))
val questions = groups.map {
    it.split("\n").map { it.toSet() }
}

println("Part one: " + questions.sumBy { it.reduce { a, b -> a union b }.size })
println("Part two: " + questions.sumBy { it.reduce { a, b -> a intersect b }.size })