import java.io.File

data class Policy(val char: Char, val first: Int, val second: Int) {
    fun matchesPartOne(password: String): Boolean =
        password.count { it == char } in first..second
    fun matchesPartTwo(password: String): Boolean =
        (password[first-1] == char) xor (password[second-1] == char)
}

val regex = Regex("(\\d+)-(\\d+) (.): (.+)")

val lines: List<Pair<Policy, String>> = File("input").readLines().map {
    val matches = regex.matchEntire(it)!!.groupValues
    Pair(Policy(matches[3][0], matches[1].toInt(), matches[2].toInt()), matches[4])
}
println("Part one: " + lines.count { it.first.matchesPartOne(it.second) })
println("Part two: " + lines.count { it.first.matchesPartTwo(it.second) })