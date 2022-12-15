import kotlin.math.abs

fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle1(input)}")
}

private val inputPattern =
    Regex("^Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)$")

private fun solvePuzzle1(input: List<String>): Int {
    val table = HashMap<Point, Char>()

    for (line in input) {
        inputPattern.find(line)?.let { matchResult ->
            val (x1, y1, x2, y2) = matchResult.groupValues.drop(1).map(String::toInt)

            val scaner = Point(x1, y1).also { table[it] = 'S' }
            val beacon = Point(x2, y2).also { table[it] = 'B' }

            for (point in determineTaxicabCircle(start = scaner, end = beacon).filter { it !in table.keys }) {
                table[point] = '#'
            }
        }
    }

    return table.filterKeys { it.y == 2000000 }.filterValues { it != 'B' }.count()
}

private fun determineTaxicabCircle(start: Point, end: Point) = buildSet {
    val length = maxOf(end.x, start.x) - minOf(end.x, start.x) + maxOf(end.y, start.y) - minOf(end.y, start.y)

    val yMin = start.y - length
    val yMax = start.y + length

    for (y in (yMin..yMax).filter { it == 2000000 }) {
        val width = length - abs(y - start.y)

        val xMin = start.x - width
        val xMax = start.x + width

        for (x in xMin..xMax) {
            add(element = Point(x, y))
        }
    }
}
