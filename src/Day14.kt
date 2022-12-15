fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle1(input)}")
    println("Part 2: ${solvePuzzle2(input)}")
}

private fun solvePuzzle1(input: List<String>): Int {
    val model = Model.parse(input)

    val xIndicies = 0..(model.xMax - model.xMin)
    val yIndicies = 0..model.yMax

    val startingPoint = Point(500 - model.xMin, 0)

    var stableUnits = 0
    var currentUnit = startingPoint

    loop@ while (true) {
        val targetPoints = currentUnit.adjacentPoints()
            .filter { it.y > currentUnit.y }
            .sortedBy { it.x }
            .toList()
            .let { (left, middle, right) -> listOf(middle, left, right) }

        for (point in targetPoints) {
            if (point.x !in xIndicies || point.y !in yIndicies) {
                break@loop
            }
            if (point !in model.data) {
                currentUnit = point
                continue@loop
            }
        }
        model.data.add(currentUnit)
        currentUnit = startingPoint
        stableUnits++
    }
    return stableUnits
}

private fun solvePuzzle2(input: List<String>): Int {
    val model = Model.parse(input)
    val startingPoint = Point(500 - model.xMin, 0)

    var stableUnits = 0
    var currentUnit = startingPoint

    loop@ while (true) {
        val targetPoints = currentUnit.adjacentPoints()
            .filter { it.y > currentUnit.y }
            .sortedBy { it.x }
            .toList()
            .let { (left, middle, right) -> listOf(middle, left, right) }

        for (point in targetPoints) {
            if (point.y < (model.yMax + 2) && point !in model.data) {
                currentUnit = point
                continue@loop
            }
        }
        if (currentUnit == startingPoint) {
            stableUnits++
            break@loop
        }
        model.data.add(currentUnit)
        currentUnit = startingPoint
        stableUnits++
    }
    return stableUnits
}

private class Model private constructor(input: List<String>) {
    val data = HashSet<Point>()

    var xMax = -1
    var xMin = -1
    var yMax = -1
    var yMin = -1

    init {
        val lines = input.flatMap { line ->
            line.split(" -> ")
                .map {
                    it.split(",").map(String::toInt).let { (x, y) ->
                        xMin = if (xMin != -1) minOf(xMin, x) else x
                        xMax = if (xMax != -1) maxOf(xMax, x) else x
                        yMin = if (yMin != -1) minOf(yMin, y) else y
                        yMax = if (yMax != -1) maxOf(yMax, y) else y
                        Point(x, y)
                    }
                }
                .windowed(2)
        }

        for ((alpha, omega) in lines) {
            when {
                alpha.x == omega.x -> {
                    for (y in minOf(alpha.y, omega.y)..maxOf(alpha.y, omega.y)) {
                        data.add(Point(alpha.x - xMin, y))
                    }
                }

                alpha.y == omega.y -> {
                    for (x in minOf(alpha.x, omega.x)..maxOf(alpha.x, omega.x)) {
                        data.add(Point(x - xMin, alpha.y))
                    }
                }
            }
        }
    }

    companion object {
        fun parse(input: List<String>) = Model(input)
    }
}
