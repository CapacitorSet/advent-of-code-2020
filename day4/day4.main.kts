import java.io.File

val requiredAttrs = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

val lines = File("input").readText().split("\n\n")
val passports = lines.map { line -> line
    .replace("\n", " ") // normalize
    .split(" ").filter { it.isNotEmpty() } // get "k:v" strings
    .map {
        val parts = it.split(":")
        Pair(parts[0], parts[1])
    }.toMap()
}

val hclRegex = Regex("^#[0-9a-f]{6}$", RegexOption.IGNORE_CASE)
val pidRegex = Regex("^\\d{9}$")
println("Part one: " + passports.count { it.keys.containsAll(requiredAttrs) })
println("Part two: " + passports.count {
    it["byr"]?.toInt() in 1920..2002
        && it["iyr"]?.toInt() in 2010..2020
        && it["eyr"]?.toInt() in 2020..2030
        && it["hcl"].toString().matches(hclRegex)
        && it["ecl"] in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        && it["pid"].toString().matches(pidRegex)
        && Regex("^(\\d+)(cm|in)$").find(it["hgt"].toString())?.groupValues.let {
            if (it == null) false else {
                val height = it[1].toInt()
                val unit = it[2]
                when (unit) {
                    "cm" -> height in 150..193
                    "in" -> height in 59..76
                    else -> false
                }
            }
        }
})