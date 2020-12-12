import java.io.File

// "target" = the one number that doesn't match
data class State(val buffer: List<Long>, val target: Long = -1)

var lines = File("input").readLines().map { it.toLong() }
val buffer = lines.take(25)
val numbers = lines.drop(25)

val target = numbers.fold(State(buffer)) { state, num ->
    val isCorrect = state.buffer.filterIndexed { idx_a, a ->
        state.buffer.filterIndexed { idx_b, b -> idx_a != idx_b && (a + b == num) }.isNotEmpty()
    }.isNotEmpty()
    val newTarget = if (!isCorrect && state.target == -1L) num else state.target
    // "ring" buffer: drop the last elt and push a new one
    val newBuffer = state.buffer.drop(1) + num
    if (!isCorrect) {
        print("$num is not correct. Possible choices: ")
        println(state.buffer.flatMap { a -> state.buffer.map { b -> a + b } }.toSortedSet())
    }
    State(newBuffer, newTarget)
}.target

println("Part one: $target")

var answer = 0L
for (begin in lines.indices) {
    var sum = 0L
    // Iterate until the cumulative sum meets or exceeds target
    for (end in lines.indices.drop(begin)) {
        val interval = lines.subList(begin, end)
        sum = interval.sum()
        if (sum >= target) {
            // "Speculatively" compute the answer. Not super elegant but eh variable scoping sucks
            answer = interval.min()!! + interval.max()!!
            break
        }
    }
    if (sum == target) {
        break
    }
}

println("Part two: $answer")