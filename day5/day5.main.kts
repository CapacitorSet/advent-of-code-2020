import java.io.File

fun passToSeatID(pass: String): Int =
    pass
        .replace("F", "0")
        .replace("B", "1")
        .replace("L", "0")
        .replace("R", "1")
        .toInt(2)

val seatIDs = File("input").readLines().map { passToSeatID(it) }.sorted()
println("Part one: " + seatIDs.last())

// Search for the missing seat ID such that sID-1 and sID+1 exist
// Reformulation: search for the (existing) seat ID such that sID+1 does not exist and sID+2 does. Then add 1.
// Reformulation: in the array of existing sIDs, find the sID such that the next element is sID+2. Then add 1.
val missingPair = seatIDs.zipWithNext().find { pair ->
    pair.second - pair.first == 2
}
println("Part two: " + (missingPair!!.first + 1))
