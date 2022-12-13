@file:OptIn(ExperimentalTime::class)

import java.util.*
import kotlin.time.ExperimentalTime

fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle(input)}")
}

private fun solvePuzzle(list: List<String>): Int {
    val squares = getSquares(list)

    val start = squares.first { it.isStart }
    val end = squares.first { it.isEnd }

    return dijkstra(start, end, squares)
}

private fun dijkstra(start: Square, end: Square, squares: List<Square>): Int {
    val queue = PriorityQueue<Square>().apply { offer(start) }
    val distances = HashMap<Square, Int>().apply { put(start, 0) }

    while (!queue.isEmpty()) {
        val curr = queue.poll()
        val dist = distances[curr]!!

        for (n in curr.getNeighbours(squares)) {
            val ndist = dist + 1
            if (ndist < (distances[n] ?: Int.MAX_VALUE)) {
                distances[n] = ndist
                queue.add(n)
            }
        }
    }
    return distances.filterKeys { it == end }.values.min()
}

private fun getSquares(input: List<String>): List<Square> {
    val squares = ArrayList<Square>()
    for ((y, line) in input.withIndex()) {
        for ((x, height) in line.withIndex()) {
            squares.add(
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
    return squares
}

data class Square(val x: Int, val y: Int) : Comparable<Square> {

    var height = 0
    var isStart = false
    var isEnd = false

    constructor(x: Int, y: Int, height: Int) : this(x, y) {
        this.height = height
    }

    fun getNeighbours(allSquares: List<Square>): List<Square> {
        val squares = ArrayList<Square>()

        copy(x = x - 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {
                val square = allSquares[indexOf]
                if (square.height <= height + 1) {
                    squares.add(square)
                }
            }
        }
        copy(x = x + 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {
                val square = allSquares[indexOf]
                if (square.height <= height + 1) {
                    squares.add(square)
                }
            }
        }
        copy(y = y - 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {
                val square = allSquares[indexOf]
                if (square.height <= height + 1) {
                    squares.add(square)
                }
            }
        }
        copy(y = y + 1).let {
            val indexOf = allSquares.indexOf(it)
            if (indexOf != -1) {
                val square = allSquares[indexOf]
                if (square.height <= height + 1) {
                    squares.add(square)
                }
            }
        }
        return squares
    }

    override operator fun compareTo(other: Square): Int {
        return if (y != other.y) y.compareTo(other.y) else x.compareTo(other.x)
    }
}
