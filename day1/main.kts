import java.io.File

fun <T> uniquePairs(arr: Array<T>): List<Pair<T, T>> {
    return arr.mapIndexed { idx, first ->
        // We only pick the items after this one to avoid creating duplicates
        val restOfTheArray = arr.slice(IntRange(idx, arr.size - 1))
        restOfTheArray.map { second -> Pair(first, second) }
    }.flatten()
}

val ints: Array<Int> = File("input").readLines().map { it.toInt() }.toTypedArray()
val pairs = uniquePairs(ints)
val pair = pairs.find { pair -> pair.first + pair.second == 2020 }!!
println("Part one: " + (pair.first * pair.second))
val triplets = ints.mapIndexed { idx, first ->
    // We only pick the items after this one to avoid processing duplicates
    val restOfTheArray = ints.sliceArray(IntRange(idx, ints.size - 1))
    val pairsSecondThird = uniquePairs(restOfTheArray)
    pairsSecondThird
        .filter { pair -> pair.first + pair.second < 2020 }
        .map { pair -> Triple(first, pair.first, pair.second) }
}.flatten()
val triplet = triplets.find { triplet -> triplet.first + triplet.second + triplet.third == 2020 }!!
println("Part two: " + (triplet.first * triplet.second * triplet.third))