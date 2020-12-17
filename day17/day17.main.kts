import java.io.File
import java.lang.Exception
import kotlin.math.abs

typealias Point = List<Int>
typealias Space = Set<Point>

val diffs = -1..+1

// Yeah, it's very ugly.
fun neighboursOf(dim: Int, p: Point): List<Point> =
    when (dim) {
        3 -> diffs.flatMap { x ->
            diffs.flatMap { y ->
                diffs
                    .filter { z -> abs(x) == 1 || abs(y) == 1 || abs(z) == 1 }
                    .map { z -> listOf(p[0] + x, p[1] + y, p[2] + z) }
            }
        }
        4 -> diffs.flatMap { x ->
            diffs.flatMap { y ->
                diffs.flatMap { z -> diffs
                    .filter { t -> abs(x) == 1 || abs(y) == 1 || abs(z) == 1 || abs(t) == 1 }
                    .map { t -> listOf(p[0] + x, p[1] + y, p[2] + z, p[3] + t) }
                }
            }
        }
        else -> throw Exception("Invalid number of dimensions")
    }

// Iterate once for a generic evolution policy
// Policies take three argument: the set of active points, the current point, and whether the point is active, and
// they return whether the point is active
fun iterate(dim: Int, space: Space, policy: (space: Space, p: Point) -> Boolean): Space =
    space.flatMap { point -> neighboursOf(dim, point) } // To all existing points and their neighbours...
        .toSet()
        .filter { policy(space, it) } // apply the policy.
        .toSet()

fun solution(_space: Space): Int {
    var space = _space
    val dim = space.first().size
    repeat(6) {
        space = iterate(dim, space) { space, point ->
            val activeNeighbours = neighboursOf(dim, point).count { it in space }
            if (point in space)
                (activeNeighbours in 2..3)
            else
                (activeNeighbours == 3)
        }
    }
    return space.size
}


val initialCells: List<Pair<Int, Int>> = File("input").readLines().flatMapIndexed { y, line ->
    line.withIndex().filter { it.value == '#' }.map { x -> Pair(x.index, y) }
}
val space3D = initialCells.map { listOf(it.first, it.second, 0) }.toSet()
val space4D = initialCells.map { listOf(it.first, it.second, 0, 0) }.toSet()

println("Part one: " + solution(space3D))
println("Part two: " + solution(space4D))