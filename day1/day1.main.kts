import java.io.File

val ints = File("input").readLines().map { it.toInt() }
part1_loop@
for (idx_1 in ints.indices)
    for (idx_2 in idx_1 until ints.size)
        if (ints[idx_1] + ints[idx_2] == 2020) {
            println("Part one: " + (ints[idx_1] * ints[idx_2]))
            break@part1_loop
        }

part2_loop@
for (idx_1 in ints.indices)
    for (idx_2 in idx_1 until ints.size)
        for (idx_3 in idx_2 until ints.size)
            if (ints[idx_1] + ints[idx_2] + ints[idx_3] == 2020) {
                println("Part two: " + (ints[idx_1] * ints[idx_2] * ints[idx_3]))
                break@part2_loop
            }