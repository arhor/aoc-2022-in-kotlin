import kotlin.math.abs

/*
# Notes

number of sides for N-dimensional figure is N * 2

point (x=2, y=2, z=5) adjacent points:

by x: (x=1, y=2, z=5)
      (x=3, y=2, z=5)

by y: (x=2, y=1, z=5)
      (x=2, y=3, z=5)

by z: (x=2, y=2, z=4)
      (x=2, y=2, z=6)

      for each point
        try to find 4 diagonal points with the same plane
*/

fun main() {
    val input = readInput {}

//    println("Part 1: ${solvePuzzle1(input)}")
    println("Part 2: ${solvePuzzle2(input)}")
}

private const val CUBE_SIDES_NUM = 6
private const val CONTACTING_SIDES_NUM = 2

private fun solvePuzzle1(input: List<String>): Int {
    val data = parseInput(input)
    val test = HashMap<Point3D, List<Point3D>>()

    for (i in data.indices) {
        val curr = data[i]
        val adjacents = ArrayList<Point3D>()

        for (j in ((i + 1)..data.lastIndex)) {
            val next = data[j]
            if (curr adjacentTo next) {
                adjacents += next
            }
        }

        test[curr] = adjacents
    }

    return (data.size * CUBE_SIDES_NUM) - test.values.sumOf { it.size * CONTACTING_SIDES_NUM }
}

private fun solvePuzzle2(input: List<String>): Int {
    val data = parseInput(input).sortedWith(compareBy({ it.x }, { it.y }, { it.z }))
    val test = HashMap<Point3D, List<Point3D>>()

    var droplets = 0

    for (i in data.indices) {
        val curr = data[i]

        val adjacents = ArrayList<Point3D>()
        val diagonals = ArrayList<Point3D>()

        for (j in ((i + 1)..data.lastIndex)) {
            val next = data[j]
            if (curr adjacentTo next) {
                adjacents += next
            }
            if (curr diagonalTo next) {
                diagonals += next
            }
        }
        test[curr] = adjacents

        if (diagonals.size == 4) {
            for (j in ((i + 1)..data.lastIndex)) {
                val next = data[j]

                if (diagonals.all { it diagonalTo next }) {
                    println("Gotcha! air droplet surroundings ${listOf(curr) + diagonals + listOf(next)}")
                    droplets++
                    break
                }
            }
        }
    }

    return (data.size * CUBE_SIDES_NUM) -
        test.values.sumOf { it.size * CONTACTING_SIDES_NUM } -
        (droplets * CUBE_SIDES_NUM)
}

private data class Point3D(val x: Int, val y: Int, val z: Int) {

    infix fun adjacentTo(other: Point3D): Boolean {
        return (x == other.x && y == other.y && abs(z - other.z) == 1)
            || (x == other.x && z == other.z && abs(y - other.y) == 1)
            || (z == other.z && y == other.y && abs(x - other.x) == 1)
    }

    infix fun diagonalTo(other: Point3D): Boolean {
        return /*(abs(x - other.x) == 1 && abs(y - other.y) == 1 && abs(z - other.z) == 1)
            || */(x == other.x && abs(y - other.y) == 1 && abs(z - other.z) == 1)
            || (y == other.y && abs(z - other.z) == 1 && abs(x - other.x) == 1)
            || (z == other.z && abs(x - other.x) == 1 && abs(y - other.y) == 1)
    }
}

private fun parseInput(input: List<String>): List<Point3D> = input.map { line ->
    line.substringBefore(" *")
        .split(",")
        .map { it.toInt() }
        .let { (x, y, z) -> Point3D(x, y, z) }
}
