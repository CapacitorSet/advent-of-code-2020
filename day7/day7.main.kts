import java.io.File

// "3 bright white bags" -> ("bright white", 3)
fun parseBags(str: String): Pair<String, Int> {
    val parts = str.split(" ")
    return Pair(parts[1] + " " + parts[2], parts[0].toInt())
}

// Apply the function f to the bag src and its descendants, with multiplicity
// (i.e. two bags of the same colour will result in applying f twice)
fun<T> bagReduce(initialValue: T, withMultiplicity: Boolean, src: String, f: (T, String) -> T): T {
    var ret = f(initialValue, src)
    bags[src]?.forEach { bag, count ->
        if (withMultiplicity)
            repeat(count) { ret = bagReduce(ret, withMultiplicity, bag, f) }
        else
            ret = bagReduce(ret, withMultiplicity, bag, f)
    }
    return ret
}

val lines = File("input").readLines()
val bags: Map<String, Map<String, Int>> = lines.map {
    val parts = it.split(Regex(" bags contain "))
    val colour = parts[0]
    val children = when (parts[1]) {
        "no other bags." -> mapOf<String, Int>()
        else -> parts[1].split(", ").map { parseBags(it) }.toMap()
    }
    Pair(colour, children)
}.toMap()

fun bagContainsShinyGold(bag: String): Boolean =
    bagReduce(false, withMultiplicity = false, src = bag)
        {hasShinyGold, bag -> hasShinyGold || bag == "shiny gold" }

// Minus one because the shiny gold bag itself matches
println("Part one: " + (bags.keys.count { bagContainsShinyGold(it) } - 1))
println("Part two: " + bagReduce(-1, withMultiplicity = true, "shiny gold") { counter, _ -> counter + 1 })