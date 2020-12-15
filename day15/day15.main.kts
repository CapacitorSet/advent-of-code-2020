import java.io.File

// Drop trailing newline
val numbers = File("input").readText().dropLast(1).split(",").map { it.toInt() }

fun solution(limit: Int): Int {
    // Rather than actually searching through a list (O(n^2)) we have a map with the last 2 occurrences of each value
    val numsSpoken = numbers.mapIndexed { idx, n -> Pair(n, intArrayOf(idx)) }.toMap().toMutableMap()
    var lastSpoken = numbers.last()
    for (i in (numbers.size until limit)) {
        val timesSpoken = numsSpoken[lastSpoken]!!
        val currentlySpoken = if (timesSpoken.size == 1) 0 else timesSpoken[1] - timesSpoken[0]
        // Optimization: keep only the 2 most recent indices in the map
        val arr = numsSpoken[currentlySpoken] ?: intArrayOf()
        numsSpoken[currentlySpoken] = when (arr.size) {
            0 -> intArrayOf(i)
            1 -> intArrayOf(arr[0], i)
            else -> intArrayOf(arr[1], i)
        }
        lastSpoken = currentlySpoken
    }
    return lastSpoken
}

println("Part one: " + solution(2020))

println("Part two: " + solution(30000000))