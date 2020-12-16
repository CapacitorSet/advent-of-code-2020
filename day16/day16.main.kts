import java.io.File

val fileParts = File("input").readText().split(Regex("\n\n")).map { it.trim().split("\n") }

data class InputParam(val first: IntRange, val second: IntRange) {
    infix fun contains(n: Int): Boolean = n in first || n in second
}

val paramRegex = Regex("(\\d+)-(\\d+) or (\\d+)-(\\d+)$")
val inputParams = fileParts[0].map {
    val values = paramRegex.find(it)!!.groupValues.drop(1).map { it.toInt() }
    InputParam(values[0]..values[1], values[2]..values[3])
}
val myTicket = fileParts[1][1].split(",").map { it.toLong() }
val nearbyTickets = fileParts[2].drop(1).map { it.split(",").map { it.toInt() } }


fun isValidField(field: Int): Boolean = inputParams.any { it contains field }
fun isValidTicket(ticket: List<Int>): Boolean = ticket.all { isValidField(it) }

println("Part one: " + nearbyTickets.sumBy { fields -> fields.filter { !isValidField(it) }.sum() })

val validTickets = nearbyTickets.filter { isValidTicket(it) }

// For each field, we store the list of possible candidates.
// Initially, any index is possible, so [0] = 0..n, [1] = 1..n, etc
val startingCandidates = inputParams.map { inputParams.indices.toList() }
val candidates: List<List<Int>> = validTickets.fold(startingCandidates) { fieldCandidates, ticketFields ->
    // The reducer narrows down the list of candidates for each field.
    // We go through each field and pick only the candidates whose range contains that field, then return the new list
    ticketFields.zip(fieldCandidates).map { pair ->
        val (field, candidates) = pair
        // filter if in either first or second range
        candidates.filter { param_idx -> inputParams[param_idx] contains field }
    }
}

// We eventually have a "triangular" list of candidates: one field has only 1 candidate, one has 2, one has 3...
// Sort it by number of candidates, then use a reducer to find the "new" candidate each time and create a map from that
val sortedCandidates = candidates.indices.zip(candidates).sortedBy { it.second.size }
val paramIndexToPosition: Map<Int, Int> = sortedCandidates.fold(mapOf<Int, Int>()) { solution, fieldCandidates ->
    // Find the one candidate that isn't yet in the map
    val paramIndex = fieldCandidates.second.subtract(solution.keys).single()
    solution + Pair(paramIndex, fieldCandidates.first)
}

// We now have a map from the index in `inputParams` to the position in the ticket.
val departureParams = 0..5 // hardcoded
val departureIndices = departureParams.map { paramIndexToPosition[it]!! }

println("Part two: " + departureIndices.map { idx -> myTicket[idx] }.reduce { a, b -> a * b })