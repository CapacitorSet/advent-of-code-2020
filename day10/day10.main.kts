import java.io.File

// Add the source at 0 jolts
val chain = listOf(0) + File("input").readLines().map { it.toInt() }.sorted()
var diffs = chain.zipWithNext { a, b -> b - a } + 3

println("Part one: " + (diffs.count { it == 1 } * diffs.count { it == 3 }))

// This function works, but not for long sequences. So we just apply to sequences that are not interrupted by a 3
fun validPaths(diffs: List<Int>): Set<List<Int>> {
    // Find the pairs of diffs that can be merged (eg. 1 + 1, or 2 + 1)
    val indices = diffs.zipWithNext()
        .mapIndexed { index, pair -> Pair(pair.first + pair.second <= 3, index) }
        .filter { it.first }.map { it.second }
    return setOf(diffs) + indices.map { idx ->
        val newValue = diffs[idx] + diffs[idx + 1]
        val newDiffs = diffs.take(idx) + newValue + diffs.drop(idx + 2)
        validPaths(newDiffs)
    }.fold(setOf()) { a, b -> a union b }
}

var result = 1L
while (diffs.isNotEmpty()) {
    // Find longest sequence not interrupted by a 3
    val subsequence = diffs.takeWhile { it != 3 }
    val paths = validPaths(subsequence)
    val numPaths = paths.size
    result *= numPaths
    // Drop it from the diffs, then start again
    diffs = diffs.dropWhile { it != 3 }.dropWhile { it == 3 }
}

println("Part two: $result")