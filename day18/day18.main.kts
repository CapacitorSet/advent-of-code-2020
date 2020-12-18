import java.io.File
import kotlin.Exception

// State from Dijkstra's shunting yard algorithm: an output queue and an operator stack
data class SYState(val output: List<Char> = emptyList(), val operators: List<Char> = emptyList())

fun dijkstraShuntingYard(tokens: String, plusHasPrecedence: Boolean): List<Char> {
    val state = tokens.fold(SYState()) { state, token ->
        when (token) {
            in '0'..'9' -> SYState(state.output + token, state.operators)
            '+', '*' -> {
                val shouldPop = if (plusHasPrecedence && token == '+')
                    ({ op: Char -> op != '(' && op != '*' })
                else
                    ({ op: Char -> op != '(' })
                SYState(
                    state.output + state.operators.takeLastWhile { shouldPop(it) }.asReversed(),
                    state.operators.dropLastWhile { shouldPop(it) } + token
                )
            }
            '(' -> SYState(state.output, state.operators + token)
            ')' -> {
                var operators = state.operators.dropLastWhile { it != '(' }
                if (operators.lastOrNull() == '(')
                    operators = operators.dropLast(1)
                SYState(
                    output = state.output + state.operators.takeLastWhile { it != '(' }.asReversed(),
                    operators = operators
                )
            }
            else -> throw Exception("Unknown token $token")
        }
    }
    return state.output + state.operators.asReversed()
}

fun solve(expression: String, plusHasPrecedence: Boolean): Long =
    dijkstraShuntingYard(expression.replace(" ", ""), plusHasPrecedence)
        .fold(emptyList<Long>()) { stack, token -> when (token) {
            in '0'..'9' -> stack + (token.toLong() - '0'.toLong())
            '+' -> stack.dropLast(2) + stack.takeLast(2).let { it[0] + it[1] }
            '*' -> stack.dropLast(2) + stack.takeLast(2).let { it[0] * it[1] }
            else -> throw Exception("Unknown token $token")
        } }.single()

// Hardcoded: all values are one digit long
val lines = File("input").readLines()

println("Part one: " + lines.map { solve(it, false) }.sum())
println("Part two: " + lines.map { solve(it, true) }.sum())