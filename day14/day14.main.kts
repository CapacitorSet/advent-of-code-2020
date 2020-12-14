import java.io.File

sealed class Insn {
    data class Mask(val mask: String) : Insn() {
        fun partOne_applyToValue(value: Long): Long {
            val zerosMask = mask.map { if (it == '0') '1' else '0' }.joinToString("").toLong(2)
            val onesMask = mask.map { if (it == '1') '1' else '0' }.joinToString("").toLong(2)
            return (value or onesMask) and zerosMask.inv()
        }
        fun partTwo_applyToValue(value: Long): String {
            val onesMask = mask.map { if (it == '1') '1' else '0' }.joinToString("").toLong(2)
            val oneMasked = (value or onesMask).toString(2).padStart(36, '0')
            return oneMasked.zip(this.mask).map { if (it.second == 'X') 'X' else it.first }.joinToString("")
        }
    }

    data class Mem(val addr: Long, val value: Long) : Insn()
}

val instrs = File("input").readLines()
    .map {
        if (it.startsWith("mask")) {
            val mask = it.drop(7) // "mask = "
            Insn.Mask(mask)
        } else {
            val parts = it.drop(4).split(Regex("\\] = ")).map { it.toLong() }
            Insn.Mem(parts[0], parts[1])
        }
    }

data class State(val mask: Insn.Mask = Insn.Mask("X"), val memory: Map<Long, Long> = mapOf())

val state1 = instrs.fold(State()) { state, insn ->
    when (insn) {
        is Insn.Mask -> State(insn, state.memory)
        is Insn.Mem -> State(
            state.mask,
            state.memory + Pair(insn.addr, state.mask.partOne_applyToValue(insn.value)))
    }
}

println("Part one: " + state1.memory.values.sum())

fun String.replaceAt(pos: Int, replacement: Char): String
    = this.take(pos) + replacement + this.drop(pos + 1)


fun walkAddressMask(address: String, f: (addr: Long) -> Unit) {
    val index = address.indexOfFirst { it == 'X' }
    if (index == -1) {
        f(address.toLong(2))
    } else {
        // Zero-replaced
        val zeroAddress = address.replaceAt(index, '0')
        walkAddressMask(zeroAddress, f)
        // One-replaced
        val oneAddress = address.replaceAt(index, '1')
        walkAddressMask(oneAddress, f)
    }
}

val state2 = instrs.fold(State()) { state, insn ->
    when (insn) {
        is Insn.Mask -> State(insn, state.memory)
        is Insn.Mem -> {
            val memory = state.memory.toMutableMap()
            val address = state.mask.partTwo_applyToValue(insn.addr)
            walkAddressMask(address) { addr -> memory[addr] = insn.value }
            State(state.mask, memory)
        }
    }
}

println("Part two: " + state2.memory.values.sum())

