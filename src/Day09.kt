fun main() {
    val input = readInput {}

    println("Part 2: " + solvePuzzle2(input))
}

private fun solvePuzzle(input: List<String>) {
    var head = Point(0, 0)
    var tail = Point(0, 0)
    val visited = LinkedHashSet<Point>().apply { add(tail) }

    println("== Initial State ==")
    println()
    drawState(head, tail)
    println()

    for (line in input) {
        val (direction, steps) = line.split(" ").let { it[0] to it[1].toInt() }

        println("== $direction $steps ==")
        println()

        if (line == "R 2") {
            println("STOP")
        }

        repeat(times = steps) {
            head = when (direction) {
                "U" -> head.copy(y = head.y + 1)
                "R" -> head.copy(x = head.x + 1)
                "D" -> head.copy(y = head.y - 1)
                "L" -> head.copy(x = head.x - 1)
                else -> throw IllegalStateException("Unsupported direction: $direction")
            }
            if (tail !in head.adjacentPoints()) {
                tail = enclose(head, tail)
                visited += tail
            }
            drawState(head, tail)
            println()
        }
    }

    println("Visited points by tail: ${visited.size}")
}

private fun solvePuzzle2(input: List<String>) {
    val rope = List(10) { Point(0, 0) }.toMutableList()
    val visited = LinkedHashSet<Point>().apply { add(rope.last()) }

    println("== Initial State ==")
    println()
    drawState(rope)
    println()

    for (line in input) {
        val (direction, steps) = line.split(" ").let { it[0] to it[1].toInt() }

        println("== $direction $steps ==")
        println()

        if (line == "R 2") {
            println("STOP")
        }

        repeat(times = steps) {
            rope[0] = when (direction) {
                "U" ->  rope.first().copy(y = rope.first().y + 1)
                "R" ->  rope.first().copy(x = rope.first().x + 1)
                "D" ->  rope.first().copy(y = rope.first().y - 1)
                "L" ->  rope.first().copy(x = rope.first().x - 1)
                else -> throw IllegalStateException("Unsupported direction: $direction")
            }
            var head = rope.first()
            rope.drop(1).withIndex().forEach { tail ->
                // ---------------------------------------------------
                if (tail.value !in head.adjacentPoints()) {

                    rope[tail.index + 1] = enclose(head, tail.value)
                    if (tail.index == 8) {
                        visited += rope[tail.index + 1]
                    }
                }
                head = rope[tail.index + 1]
                // ---------------------------------------------------
            }
            drawState(rope)
            println()
        }
    }

    println("Visited points by tail: ${visited.size}")
}

private fun drawState(head: Point, tail: Point) {
    println(
        List(6) { y ->
            List(6) { x ->
                if (x == head.x && y == head.y) {
                    "H"
                } else if (x == tail.x && y == tail.y) {
                    "T"
                } else {
                    "."
                }
            }.joinToString(separator = "")
        }.reversed().joinToString(separator = "\n")
    )
}

private fun drawState(rope: List<Point>) {
    println(
        List(6) { y ->
            List(6) { x ->
                if (x == rope.first().x && y == rope.first().y) {
                    "H"
                } else if (rope.find { it.x == x && it.y == y } != null) {
                    rope.indexOfFirst { it.x == x && it.y == y }.toString()
                } else {
                    "."
                }
            }.joinToString(separator = "")
        }.reversed().joinToString(separator = "\n")
    )
}

private fun enclose(head: Point, tail: Point): Point {
    return when {
        tail.x == head.x -> {
            // get rid of copying
            if (tail.y < head.y) {
                tail.copy(y = tail.y + 1)
            } else {
                tail.copy(y = tail.y - 1)
            }
        }

        tail.y == head.y -> {
            // get rid of copying
            if (tail.x < head.x) {
                tail.copy(x = tail.x + 1)
            } else {
                tail.copy(x = tail.x - 1)
            }
        }

        else -> when {
            tail.y < head.y && tail.x < head.x -> tail.copy(y = tail.y + 1, x = tail.x + 1)
            tail.y < head.y && tail.x > head.x -> tail.copy(y = tail.y + 1, x = tail.x - 1)
            tail.y > head.y && tail.x < head.x -> tail.copy(y = tail.y - 1, x = tail.x + 1)
            tail.y > head.y && tail.x > head.x -> tail.copy(y = tail.y - 1, x = tail.x - 1)
            else -> throw IllegalStateException()
        }
    }
}
