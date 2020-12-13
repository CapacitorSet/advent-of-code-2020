import java.io.File
import kotlin.math.abs

// dir: 0 = North, 1 = East, 2 = South, 3 = West
data class State1(val x: Int = 0, val y: Int = 0, val dir: Int = 1) {
    fun rotateDirCW(deg: Int): State1 =
        State1(x, y, dir = Math.floorMod(this.dir + deg / 90, 4)) // floorMod avoids going into the negative
}
data class State2(val x: Int, val y: Int, val posX: Int = 0, val posY: Int = 0) {
    fun rotateSelfCW(deg: Int): State2 =
        when (deg / 90) {
            0 -> this
            1, -3 -> State2(x = this.y, y = -this.x, this.posX, this.posY)
            2, -2 -> State2(x = -this.x, y = -this.y, this.posX, this.posY)
            3, -1 -> State2(x = -this.y, y = this.x, this.posX, this.posY)
            else -> throw Exception("Unexpected rotation $deg")
        }
}

val insns = File("input").readLines()
        .map { Pair(it[0], it.drop(1).toInt()) }

// I made a function just so 'F' could be handled recursively lol
fun part1Reducer(state: State1, insn: Pair<Char, Int>): State1 =
    when (insn.first) {
        'N' -> State1(state.x, state.y + insn.second, state.dir)
        'E' -> State1(state.x + insn.second, state.y, state.dir)
        'S' -> State1(state.x, state.y - insn.second, state.dir)
        'W' -> State1(state.x - insn.second, state.y, state.dir)
        'L' -> state.rotateDirCW(-insn.second)
        'R' -> state.rotateDirCW(insn.second)
        'F' -> part1Reducer(state, Pair(when (state.dir) {
            0 -> 'N'; 1 -> 'E'; 2 -> 'S'; 3 -> 'W'; else -> throw Exception("Unexpected dir " + state.dir)
        }, insn.second))
        else -> throw Exception("Unexpected insn " + insn.first)
    }

val state1 = insns.fold(State1()) { state, insn -> part1Reducer(state, insn) }

println("Part one: " + (abs(state1.x) + abs(state1.y)))

val state2 = insns.fold(State2(10, 1)) { state, insn ->
    when (insn.first) {
        'N' -> State2(state.x, state.y + insn.second, state.posX, state.posY)
        'E' -> State2(state.x + insn.second, state.y, state.posX, state.posY)
        'S' -> State2(state.x, state.y - insn.second, state.posX, state.posY)
        'W' -> State2(state.x - insn.second, state.y, state.posX, state.posY)
        'L' -> state.rotateSelfCW(-insn.second)
        'R' -> state.rotateSelfCW(insn.second)
        'F' -> State2(state.x, state.y, state.posX + insn.second * state.x, state.posY + insn.second * state.y)
        else -> throw Exception("Unexpected insn " + insn.first)
    }
}

println("Part two: " + (abs(state2.posX) + abs(state2.posY)))