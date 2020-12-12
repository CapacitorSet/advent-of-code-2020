import java.io.File

// Returns a success code (true if no infinite loops) and the accumulator
fun execute(program: List<Pair<String, Int>>): Pair<Boolean, Int> {
    var program_counter = 0
    var accumulator = 0
    val positions_visited = mutableSetOf<Int>()
    val success: Boolean
    while (true) {
        if (!positions_visited.add(program_counter)) {
            success = false
            break
        }
        if (program_counter >= program.size) {
            success = true
            break
        }
        val insn = program[program_counter]
        when (insn.first) {
            "nop" -> program_counter++
            "acc" -> { program_counter++; accumulator += insn.second }
            "jmp" -> program_counter += insn.second
        }
    }
    return Pair(success, accumulator)
}

var program: MutableList<Pair<String, Int>> = File("input").readLines().map {
    val parts = it.split(" ")
    Pair(parts[0], parts[1].toInt())
}.toMutableList()

println("Part one: " + execute(program).second)

var accumulator = 0
for (pos in program.indices) {
    val insn = program[pos]
    when (insn.first) {
        "nop" -> program[pos] = Pair("jmp", insn.second)
        "jmp" -> program[pos] = Pair("nop", insn.second)
    }
    val status = execute(program)
    val success = status.first
    accumulator = status.second
    // Undo change
    program[pos] = insn
    if (success)
        break
}

println("Part two: $accumulator")