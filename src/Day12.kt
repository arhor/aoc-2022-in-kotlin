import java.util.*

fun main() {
    val input = readInput {}

    val yIndicies = 0..input.lastIndex
    val xIndicies = 0..input.first().lastIndex

    fun validNeighbours(curr: Point, next: Point): Boolean {
        if (next.x in xIndicies && next.y in yIndicies) {
            val currHeight = determineHeight(input[curr.y][curr.x])
            val nextHeight = determineHeight(input[next.y][next.x])

            return nextHeight <= currHeight + 1
        }
        return false
    }

    val data = input.withIndex().flatMap { (y, line) -> line.withIndex().map { (x, _) -> Point(x, y) } }

    println("Part 1: ${solvePuzzle1(input, data, ::validNeighbours)}")
    println("Part 2: ${solvePuzzle2(input, data, ::validNeighbours)}")
}

private fun solvePuzzle1(input: List<String>, data: List<Point>, validNeighbours: Point.(Point) -> Boolean): Double {
    val alpha = data.first { input[it.y][it.x] == 'S' }
    val omega = data.first { input[it.y][it.x] == 'E' }

    return dijkstra(alpha, omega, validNeighbours)
}

private fun solvePuzzle2(input: List<String>, data: List<Point>, validNeighbours: Point.(Point) -> Boolean): Double {
    val alpha = data.filter { input[it.y][it.x].let { height -> height == 'S' || height == 'a' } }
    val omega = data.first { input[it.y][it.x] == 'E' }

    return alpha.minOf { dijkstra(it, omega, validNeighbours) }
}

private fun determineHeight(value: Char) = when (value) {
    'S' -> 'a'
    'E' -> 'z'
    else -> value
}.code

private fun dijkstra(alpha: Point, omega: Point, validNeighbours: (Point, Point) -> Boolean): Double {
    val unvisited = PriorityQueue<Point>().apply { offer(alpha) }
    val distances = HashMap<Point, Double>().apply { put(alpha, 0.0) }.withDefault { Double.POSITIVE_INFINITY }

    while (!unvisited.isEmpty()) {
        val curr = unvisited.poll()
        val dist = distances.getValue(curr) + 1

        for (next in curr.adjacentPoints(self = false, diagonal = false)) {
            if (validNeighbours(curr, next) && dist < distances.getValue(next)) {
                distances[next] = dist
                unvisited.add(next)
            }
        }
    }
    return distances.filterKeys { it == omega }.values.minOrNull() ?: Double.POSITIVE_INFINITY
}
