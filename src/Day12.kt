import java.util.*

fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle(input)}")
}

private fun solvePuzzle(list: List<String>): Double {
    val squares = getSquares(list)

    val start = squares.first { it.isStart }
    val end = squares.first { it.isEnd }

    return dijkstra(start, end, squares)
}

private fun dijkstra(start: Square, end: Square, squares: List<Square>): Double {
    val queue = PriorityQueue<Square>().apply { offer(start) }
    val distances = HashMap<Square, Double>().apply { put(start, 0.0) }

    while (!queue.isEmpty()) {
        val currPoint = queue.poll()
        val oldDistance = distances[currPoint]!!
        val newDistance = oldDistance + 1

        for (nextPoint in currPoint.getNeighbours(squares)) {
            if (newDistance < (distances[nextPoint] ?: Double.POSITIVE_INFINITY)) {
                distances[nextPoint] = newDistance
                queue.add(nextPoint)
            }
        }
    }
    return distances.filterKeys { it == end }.values.min()
}

private fun getSquares(input: List<String>): List<Square> = buildList {
    for ((y, line) in input.withIndex()) {
        for ((x, height) in line.withIndex()) {
            add(
                Square(x, y, height.code).also {
                    when (height) {
                        'S' -> {
                            it.height = 'a'.code
                            it.isStart = true
                        }

                        'E' -> {
                            it.height = 'z'.code
                            it.isEnd = true
                        }
                    }
                }
            )
        }
    }
}

data class Square(val x: Int, val y: Int) : Comparable<Square> {

    var height = 0
    var isStart = false
    var isEnd = false

    constructor(x: Int, y: Int, height: Int) : this(x, y) {
        this.height = height
    }

    fun getNeighbours(allSquares: List<Square>): List<Square> = buildList {
        copy(x = x - 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {                   // check that point in the area
                val square = allSquares[indexOf]
                if (square.height <= height + 1) { // check that point haight applicable
                    add(square)
                }
            }
        }
        copy(x = x + 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {                   // check that point in the area
                val square = allSquares[indexOf]
                if (square.height <= height + 1) { // check that point haight applicable
                    add(square)
                }
            }
        }
        copy(y = y - 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {                   // check that point in the area
                val square = allSquares[indexOf]
                if (square.height <= height + 1) { // check that point haight applicable
                    add(square)
                }
            }
        }
        copy(y = y + 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {                   // check that point in the area
                val square = allSquares[indexOf]
                if (square.height <= height + 1) { // check that point haight applicable
                    add(square)
                }
            }
        }
    }

    override operator fun compareTo(other: Square): Int {
        return if (y != other.y) y.compareTo(other.y) else x.compareTo(other.x)
    }
}
