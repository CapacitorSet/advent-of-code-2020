import java.io.File

// "3 bright white bags" -> ("bright white", 3)
fun parseBags(str: String): Pair<String, Int> {
    val parts = str.split(" ")
    return Pair(parts[1] + " " + parts[2], parts[0].toInt())
}

// Does the haystackBag contain needle eventually?
// (This could be memoized but the input is small enough that this isn't worth the additional complexity.)
fun bagContains(needle: String, haystackBag: String): Boolean {
    return haystackBag == needle
        || bags[haystackBag]!!.keys.any { bagContains(needle, it) }
}

// Apply the function f to the bag src and its descendants, with multiplicity
// (i.e. two bags of the same colour will result in applying f twice)
fun<T> bagWalk(src: String, f: (String) -> T) {
    f(src)
    bags[src]?.forEach { bag, count ->
        repeat(count) { bagWalk(bag, f) }
    }
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

// Minus one because the shiny gold bag itself matches
println("Part one: " + (bags.keys.count { bagContains("shiny gold", it) } - 1))
var bagCounter = 0
bagWalk("shiny gold") { bagCounter++ }
println("Part two: " + (bagCounter - 1))