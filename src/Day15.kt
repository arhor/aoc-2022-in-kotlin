import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.streams.asStream
import kotlin.system.measureTimeMillis

//fun main() {
//    val input = readInput {}
//
//    println("Part 1: ${solvePuzzle2(input)}")
//}

private const val TARGET_Y = 2_000_000
private const val LIMIT = 4_000_000
private val inputPattern =
    Regex("^Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)$")

private fun solvePuzzle1(input: List<String>): Int {
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

private fun solvePuzzle2(input: List<String>): Int {
    val table = HashMap<Point, Int>()
    val beacons = HashSet<Point>()

    val xMin = 0
    val xMax = LIMIT
    val yMin = 0
    val yMax = LIMIT

    for (line in input) {
        val (x1, y1, x2, y2) = inputPattern.find(line)!!.groupValues.drop(1).map(String::toInt)
        val radius = determineTaxicabDistance(x1, y1, x2, y2)

        val scaner = Point(x1, y1)
        val beacon = Point(x2, y2)

        table[scaner] = radius
        beacons.add(beacon)
    }

    var y = yMin
    y@ while (y++ <= yMax) {
        println("current y: ${y.toString().padStart(7)}")
        var x = xMin
        x@ while (x++ <= xMax) {
            val distances = table.map { (scaner, radius) ->
                val dist = determineTaxicabDistance(x1 = x, y1 = y, x2 = scaner.x, y2 = scaner.y)
                val diff = radius - dist

                scaner to diff
            }.filter { it.second >= 0 }.sortedBy { it.second }

            if (distances.isNotEmpty()) {
                val (excitingPoint, diff) = distances.first()

                if (excitingPoint.x > x) {
                    x += ((excitingPoint.x - x).absoluteValue + diff)
                }

                continue@x
            }
            return x * 4000000 + y
        }
    }
    return 0
}

private fun determineTaxicabDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int = abs(x1 - x2) + abs(y1 - y2)

fun Point.tuningFrequency() = x * 4_000_000L + y

data class SensorMeasurement(val sensor: Point, val beacon: Point) {

    fun sensorRangePerY(): Sequence<Pair<Int, IntRange>> {
        val distance = sensor.manhattanDistanceTo(beacon)

        return (0..distance).asSequence().flatMap { yOffset ->
            val xOffset = distance - yOffset
            val xRange = (sensor.x - xOffset)..(sensor.x + xOffset)

            sequence {
                yield(sensor.y + yOffset to xRange)
                yield(sensor.y - yOffset to xRange)
            }
        }
    }
}

fun main() {
    val lines = readInput { }

    val cave: Cave
    measureTimeMillis {
        cave = parseModel(lines)
    }.let { println("Parsing and optimizing cave in $it ms") }

    val y = 2000000
    val cannotBeBeaconAtY: Int
    measureTimeMillis {
        cannotBeBeaconAtY = cave.cannotBeBeaconAtY(y)
    }.let { println("Checking y for beacon stuff in $it ms") }
    println("At y=$y, $cannotBeBeaconAtY positions cannot be beacons")

    val beaconPos: Point
    measureTimeMillis {
        beaconPos = cave.findBeaconIn(0..4_000_000)
    }.let { println("Found beacon pos in $it ms") }
    println("The beacon must be at $beaconPos, its tuning frequency is ${beaconPos.tuningFrequency()}")
}

private fun parseModel(data: List<String>): Cave =
    data.map { inputPattern.matchEntire(it)!!.groupValues }
        .map { (_, sensorX, sensorY, beaconX, beaconY) ->
            val sensor = Point(sensorX.toInt(), sensorY.toInt())
            val beacon = Point(beaconX.toInt(), beaconY.toInt())

            SensorMeasurement(sensor, beacon)
        }
        .let(::Cave)

data class Cave(private val sensorMeasurements: List<SensorMeasurement>) {
    private val minX: Int
    private val maxX: Int
    private val minY: Int
    private val maxY: Int

    private val grid = HashMap<Point, Entity>()
    private val sensorCoverageRangesY = HashMap<Int, MutableList<IntRange>>()

    init {
        sensorMeasurements.forEach {
            grid[it.sensor] = Entity.SENSOR
            grid[it.beacon] = Entity.BEACON
        }

        // Coverage
        sensorMeasurements.flatMap { it.sensorRangePerY() }.forEach { (y, xRange) ->
            sensorCoverageRangesY.getOrPut(y, ::ArrayList).add(xRange)
        }

        sensorCoverageRangesY.keys.forEach {
            sensorCoverageRangesY[it] = sensorCoverageRangesY[it]!!.compressed().toMutableList()
        }


        // Limits
        sequenceOf(
            sensorMeasurements.map { it.beacon.y },
            sensorCoverageRangesY.keys,
        )
            .flatten()
            .asStream()
            .mapToInt { it }
            .summaryStatistics()
            .let {
                minY = it.min
                maxY = it.max
            }

        sequenceOf(
            sensorMeasurements.map { it.beacon.x },
            sensorCoverageRangesY.values.flatMap { it.flatMap { xRange -> listOf(xRange.first, xRange.last) } }
        )
            .flatten()
            .asStream()
            .mapToInt { it }
            .summaryStatistics()
            .let {
                minX = it.min
                maxX = it.max
            }
    }

    fun cannotBeBeaconAtY(y: Int) = (minX..maxX).map { Point(it, y) }.count(::cannotBeBeacon)

    fun findBeaconIn(range: IntRange): Point = range.flatMap { y ->
        searchRanges(sensorCoverageRangesY[y] ?: emptyList())
            .restrictToView(range)
            .flatten()
            .map { x -> Point(x, y) }
    }.first(::canBeNewBeacon)

    private fun isCoveredBySensor(point: Point) = sensorCoverageRangesY[point.y]?.any { point.x in it } ?: false

    private fun cannotBeBeacon(point: Point) = when (grid[point]) {
        Entity.SENSOR -> true
        Entity.BEACON -> false
        else -> isCoveredBySensor(point)
    }

    private fun canBeNewBeacon(point: Point) = when (grid[point]) {
        Entity.SENSOR -> false
        Entity.BEACON -> false
        else -> !isCoveredBySensor(point)
    }

    private fun searchRanges(sensorCoveredRange: List<IntRange>): List<IntRange> {
        if (sensorCoveredRange.isEmpty()) {
            return listOf(minX..maxX)
        }

        val searchRanges = mutableListOf(minX until sensorCoveredRange.first().first)

        searchRanges += sensorCoveredRange.zipWithNext { left, right ->
            left.last + 1 until right.first
        }

        searchRanges += sensorCoveredRange.last().last + 1..maxX

        return searchRanges
    }

    private fun List<IntRange>.compressed(): List<IntRange> = sortedBy { it.first }.fold(ArrayList()) { result, range ->
        val lastCompressed = result.lastOrNull()

        if (lastCompressed?.overlaps(range) == true) {
            result[result.lastIndex] = lastCompressed.merge(range)
        } else {
            result += range
        }
        result
    }

    private fun List<IntRange>.restrictToView(view: IntRange): List<IntRange> {
        val result = ArrayList<IntRange>()

        for (range in this) {
            when {
                range.first  in view && range.last  in view -> result += range
                range.first  in view && range.last !in view -> result += range.first..view.last
                range.first !in view && range.last  in view -> result += view.first..range.last
            }
        }
        return result
    }
}

enum class Entity { SENSOR, BEACON }
