import java.util.*

fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle1(input)}")
}

private val inputPattern =
    Regex("^Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)$")

private enum class BeaconType { S, B }

private fun solvePuzzle1(input: List<String>) {
    val table = TreeMap<Point, BeaconType>()

    for (line in input) {
        inputPattern.find(line)?.let {
            val (x1, y1, x2, y2) = it.groupValues.drop(1).map(String::toInt)

            table[Point(x1, y1)] = BeaconType.S
            table[Point(x2, y2)] = BeaconType.B
        }
    }
}
