import java.io.File

data class State(val rowid: Int = 0, val treeCount: Int = 0)

fun treeCountReducer(slope: Int): (State, String) -> State =
    { state: State, line: String ->
        // rem (mod) accounts for "repeating".
        val index = (state.rowid * slope).rem(line.length)
        State(state.rowid + 1,
            if (line[index] == '#') state.treeCount + 1 else state.treeCount)
    }

fun treeCount(lines: List<String>, slope: Int): Int =
    lines.fold(State(), treeCountReducer(slope)).treeCount

val lines = File("input").readLines()

val slope1 = treeCount(lines, 1)
val slope3 = treeCount(lines, 3)
val slope5 = treeCount(lines, 5)
val slope7 = treeCount(lines, 7)
val slope1d1 = treeCount(lines.filterIndexed { idx, _ -> idx.rem(2) == 0 }, 1)
println("Part one: $slope3")
println("Part two: " + (slope1.toLong()*slope3.toLong()*slope5.toLong()*slope7.toLong()*slope1d1.toLong()))