fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle1(input)}")
}

private fun solvePuzzle1(input: List<String>): Int {
    var xMax: Int? = null
    var xMin: Int? = null
    var yMax: Int? = null
    var yMin: Int? = null

    fun parsePoint(text: String) = text.split(",").map(String::toInt).let { (x, y) -> Point(x, y) }.also { (x, y) ->
        xMin = xMin?.let { minOf(it, x) } ?: x
        xMax = xMax?.let { maxOf(it, x) } ?: x
        yMin = yMin?.let { minOf(it, y) } ?: y
        yMax = yMax?.let { maxOf(it, y) } ?: y
    }

    val lines = input.flatMap { it.split(" -> ").map(::parsePoint).windowed(2) }

    val xIndicies = 0..(xMax!! - xMin!!)
    val yIndicies = 0..yMax!!

    fun Point.outOfRange(): Boolean = x !in xIndicies || y !in yIndicies

    val model = HashMap<Point, Boolean>()

    for ((alpha, omega) in lines) {
        when {
            alpha.x == omega.x -> {
                for (y in minOf(alpha.y, omega.y)..maxOf(alpha.y, omega.y)) {
                    model[Point(alpha.x - xMin!!, y)] = true
                }
            }

            alpha.y == omega.y -> {
                for (x in minOf(alpha.x, omega.x)..maxOf(alpha.x, omega.x)) {
                    model[Point(x - xMin!!, alpha.y)] = true
                }
            }
        }
    }
    val startingPoint = Point(500 - xMin!!, 0)

    var stableUnits = 0
    var currentUnit = startingPoint

    loop@ while (true) {
        val targetPoints = currentUnit.adjacentPoints(self = false, diagonal = true)
            .filter { it.y > currentUnit.y }
            .sortedBy { it.x }
            .toList()
            .let { (left, middle, right) -> listOf(middle, left, right) }

        for (point in targetPoints) {
            if (point.outOfRange()) {
                break@loop
            }
            if (model[point] != true) {
                currentUnit = point
                continue@loop
            }
        }
        model[currentUnit] = true
        currentUnit = startingPoint
        stableUnits++
    }
    return stableUnits
}
