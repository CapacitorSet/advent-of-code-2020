import java.io.File

// "target" = the one number that doesn't match
data class State(val buffer: List<Long>, val target: Long = -1)

var lines = File("input").readLines().map { it.toLong() }
val buffer = lines.take(25)
val numbers = lines.drop(25)

fun List<Long>.anyIndexed(f: (index: Int, item: Long) -> Boolean): Boolean {
    return this.filterIndexed(f).isNotEmpty()
}

val target = numbers.fold(State(buffer)) { state, num ->
    val isCorrect = state.buffer.anyIndexed { idx_a, a ->
        state.buffer.anyIndexed { idx_b, b -> idx_a != idx_b && (a + b == num) }
    }
    State(
        target = if (!isCorrect && state.target == -1L) num else state.target,
        // "ring" buffer: drop the last elt and push a new one
        buffer = state.buffer.drop(1) + num
    )
}.target

println("Part one: $target")

for_loop@
// Iterate until the cumulative sum meets or exceeds target
for (begin in lines.indices) {
    for (end in begin until lines.size) {
        val interval = lines.subList(begin, end)
        val sum = interval.sum()
        when {
            (sum > target) -> break
            (sum == target) -> {
                println("Part two: " + (interval.min()!! + interval.max()!!))
                break@for_loop
            }
        }
    }
}