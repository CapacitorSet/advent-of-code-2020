import java.io.File

val fileLayout = File("input").readLines().map { it.toCharArray() }

typealias Row = CharArray
data class PlaneAccumulator(val rows: List<Row> = listOf(), val changed: Boolean = false)
data class RowAccumulator(val row: Row = charArrayOf(), val changed: Boolean = false)

// Iterate once for a generic evolution policy
fun iterate(
    plane: List<Row>,
    policy: (plane: List<Row>, row_id: Int, col_id: Int) -> Char): PlaneAccumulator
    = plane.foldIndexed(PlaneAccumulator(), { row_id, acc, row ->
        val result = row.foldIndexed(RowAccumulator()) { col_id, acc, char ->
            val newChar = policy(plane, row_id, col_id)
            RowAccumulator(acc.row + newChar, acc.changed || (char != newChar))
        }
        PlaneAccumulator(acc.rows + result.row, acc.changed || result.changed)
    })

fun iterateUntilStable(
    policy: (plane: List<Row>, row_id: Int, col_id: Int) -> Char): List<Row> {
    var layout = fileLayout
    while (true) {
        val result = iterate(layout, policy)
        layout = result.rows
        if (!result.changed)
            break
    }
    return layout
}

val partOneResult = iterateUntilStable { plane, row_id, col_id ->
    val char = plane[row_id][col_id]
    val neighbours = listOf(
        plane.getOrNull(row_id - 1, col_id - 1),
        plane.getOrNull(row_id - 1, col_id),
        plane.getOrNull(row_id - 1, col_id + 1),
        plane.getOrNull(row_id, col_id - 1),
        plane.getOrNull(row_id, col_id + 1),
        plane.getOrNull(row_id + 1, col_id - 1),
        plane.getOrNull(row_id + 1, col_id),
        plane.getOrNull(row_id + 1, col_id + 1)
    )
    val numNeighbours = neighbours.count { it == '#' }
    if (char == '.') '.' else when (numNeighbours) {
        0 -> '#'
        1, 2, 3 -> char
        else -> 'L'
    }
}

println("Part one: " + partOneResult.sumBy { it.count { it == '#' } })

fileLayout.forEach { println(String(it)) }
println()


val partTwoResult = iterateUntilStable { plane, row_id, col_id ->
    var neighbours = mutableListOf<Char>()
    val num_cols = plane[row_id].size
    val num_rows = plane.size
    // Vertical candidates
    var col_idx = col_id - 1
    while (col_idx >= 0) {
        val char = plane.getOrNull(row_id, col_idx--)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    col_idx = col_id + 1
    while (col_idx < num_cols) {
        val char = plane.getOrNull(row_id, col_idx++)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    // Horizontal candidates
    var row_idx = row_id - 1
    while (row_idx >= 0) {
        val char = plane.getOrNull(row_idx--, col_id)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    row_idx = row_id + 1
    while (row_idx < num_rows) {
        val char = plane.getOrNull(row_idx++, col_id)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    // Diagonal candidates
    col_idx = col_id - 1
    row_idx = row_id - 1
    while (row_idx >= 0 && col_idx >= 0) {
        val char = plane.getOrNull(row_idx--, col_idx--)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    row_idx = row_id - 1
    col_idx = col_id + 1
    while (row_idx >= 0 && col_idx < num_cols) {
        val char = plane.getOrNull(row_idx--, col_idx++)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    row_idx = row_id + 1
    col_idx = col_id - 1
    while (row_idx < num_rows && col_idx >= 0) {
        val char = plane.getOrNull(row_idx++, col_idx--)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    row_idx = row_id + 1
    col_idx = col_id + 1
    while (row_idx < num_rows && col_idx < num_cols) {
        val char = plane.getOrNull(row_idx++, col_idx++)
        when (char) {
            null -> break
            '.' -> continue
            'L', '#' -> { neighbours.add(char); break }
        }
    }
    val char = plane[row_id][col_id]
    if (char == '.') '.' else when (neighbours.count { it == '#' }) {
        0 -> '#'
        1, 2, 3, 4 -> char
        else -> 'L'
    }
}

println("Part two: " + partTwoResult.sumBy { it.count { it == '#' } })

fun List<CharArray>.getOrNull(row_idx: Int, col_idx: Int): Char? =
    this.getOrNull(row_idx)?.getOrNull(col_idx)
