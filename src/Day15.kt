import kotlin.math.abs

fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzleAlternative(input)}")
}

private const val TARGET_Y = 2_000_000
private val inputPattern =
    Regex("^Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)$")

private fun solvePuzzleAlternative(input: List<String>): Int {
    val table = HashMap<Point, Int>()
    val beacons = HashSet<Point>()

    var xMin = Int.MIN_VALUE
    var xMax = Int.MIN_VALUE
    var yMin = Int.MIN_VALUE
    var yMax = Int.MIN_VALUE

    for (line in input) {
        inputPattern.find(line)?.let { matchResult ->
            val (x1, y1, x2, y2) = matchResult.groupValues.drop(1).map(String::toInt)
            val radius = determineTaxicabDistance(x1, y1, x2, y2)

            xMin = if (xMin != Int.MIN_VALUE) minOf(xMin, x1 - radius) else x1 - radius
            xMax = if (xMax != Int.MIN_VALUE) maxOf(xMax, x1 + radius) else x1 + radius
            yMin = if (yMin != Int.MIN_VALUE) minOf(yMin, y1 - radius) else y1 - radius
            yMax = if (yMax != Int.MIN_VALUE) maxOf(yMax, y1 + radius) else y1 + radius

            val scaner = Point(x1, y1)
            val beacon = Point(x2, y2)

            table[scaner] = radius
            beacons.add(beacon)
        }
    }

    var cellsUnderScaner = 0
    for (x in xMin..xMax) {
        for ((scaner, radius) in table) {
            val dist = determineTaxicabDistance(x1 = x, y1 = TARGET_Y, x2 = scaner.x, y2 = scaner.y)
            val diff = radius - dist

            if (diff >= 0) {
                cellsUnderScaner++
                break
            }
        }
    }
    return cellsUnderScaner - beacons.count { it.y == TARGET_Y }
}

private fun determineTaxicabDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = abs(x1 - x2) + abs(y1 - y2)
