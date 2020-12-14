import java.io.File

fun minutesUntil(bus: Int, ts: Int) = bus - (ts % bus)

val lines = File("input").readLines()
val ts = lines[0].toInt()
// Kinda ugly but eh
val busesWithIndex = lines[1].split(",")
        .flatMapIndexed { idx, bus ->
            if (bus == "x")
                listOf()
            else
                listOf(Pair(bus.toInt(), idx))
        }
val buses = busesWithIndex.map { it.first }

val earliestBus = buses.minBy { minutesUntil(it, ts) }!!
println("Part one: " + (earliestBus*minutesUntil(earliestBus, ts)))

// I cheated because I know essentially nothing about the Chinese remainder theorem
// https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin

// returns x where (a * x) % b == 1
fun multInv(a: Long, b: Long): Long {
    if (b == 1L) return 1
    var aa = a
    var bb = b
    var x0 = 0L
    var x1 = 1L
    while (aa > 1) {
        val q = aa / bb
        var t = bb
        bb = aa % bb
        aa = t
        t = x0
        x0 = x1 - q * x0
        x1 = t
    }
    if (x1 < 0) x1 += b
    return x1
}

fun chineseRemainder(n: List<Int>, a: List<Int>): Long {
    val prod = n.fold(1L) { acc, i -> acc * i }
    val sum = n.indices.map { i ->
        val p = prod / n[i]
        a[i].toLong() * multInv(p, n[i].toLong()) * p
    }.sum()
    return sum % prod
}

val moduli = busesWithIndex.map { it.first - it.second }
println("Part two: " + chineseRemainder(buses, moduli))