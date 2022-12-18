fun main() {
    val input = readInput {}
    val (visibleTrees, bestScenicScore) = solvePuzzle(input)

    println("Part 1: $visibleTrees")
    println("Part 2: $bestScenicScore")
}

private fun solvePuzzle(input: List<String>): Pair<Int, Int> {
    val data = input.map { it.map(Char::digitToInt) }

    val rows = data.size
    val cols = data.first().size

    var visibleTrees = (rows + cols) * 2 - 4
    var bestScenicScore = 0

    for (y in 1 until rows - 1) {
        for (x in 1 until cols - 1) {
            val value = data[y][x]
            val lines = data.iterateToEdges(x, y)
            val lessThanCurrentValue = { other: Int -> other < value }

            if (lines.any { it.all(lessThanCurrentValue) }) {
                visibleTrees++
                lines.map { line -> line.countUntil(lessThanCurrentValue) }.reduce { a, b -> a * b }.also {
                    if (it > bestScenicScore) {
                        bestScenicScore = it
                    }
                }
            }
        }
    }
    return visibleTrees to bestScenicScore
}

private fun <T> List<List<T>>.iterateToEdges(x: Int, y: Int): Sequence<List<T>> {
    val data = this
    return sequence {
        yield(value = data[y].slice(0 until x).reversed())
        yield(value = data[y].slice((x + 1) until data[y].size))
        yield(value = (0 until y).map { data[it][x] }.reversed())
        yield(value = ((y + 1) until data.size).map { data[it][x] })
    }
}
