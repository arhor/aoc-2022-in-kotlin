fun main() {
    val input = readInput {}

    println("Part 1: " + solvePuzzle(input, tailSize = 1))
    println("Part 2: " + solvePuzzle(input, tailSize = 9))
}

private fun solvePuzzle(input: List<String>, tailSize: Int): Int {
    val rope = MutableList(1 + tailSize) { Point(0, 0) }
    val visited = HashSet<Point>().apply { add(rope.last()) }

    for (line in input) {
        val (direction, steps) = line.split(" ").let { it[0] to it[1].toInt() }

        repeat(times = steps) {
            for ((index, curr) in rope.withIndex()) {
                rope[index] = if (index == 0) {
                    when (direction) {
                        "U" -> curr.copy(y = curr.y + 1)
                        "R" -> curr.copy(x = curr.x + 1)
                        "D" -> curr.copy(y = curr.y - 1)
                        "L" -> curr.copy(x = curr.x - 1)
                        else -> throw IllegalStateException("Unsupported direction: $direction")
                    }
                } else {
                    val prev = rope[index - 1]
                    if (curr !in prev.adjacentPoints()) {
                        curr.enclosedTo(prev).also {
                            if (index == rope.lastIndex) {
                                visited += it
                            }
                        }
                    } else {
                        curr
                    }
                }
            }
        }
    }
    return visited.size
}
