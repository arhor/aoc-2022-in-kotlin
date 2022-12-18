import kotlin.math.abs

data class Point(val x: Int, val y: Int) : Comparable<Point> {

    override fun compareTo(other: Point): Int = COMPARATOR.compare(this, other)

    fun adjacentPoints(diagonal: Boolean = true) = sequence {
        yield(value = copy(y = y + 1))
        if (diagonal) yield(value = copy(x = x + 1, y = y + 1))
        yield(value = copy(x = x + 1))
        if (diagonal) yield(value = copy(x = x + 1, y = y - 1))
        yield(value = copy(y = y - 1))
        if (diagonal) yield(value = copy(x = x - 1, y = y - 1))
        yield(value = copy(x = x - 1))
        if (diagonal) yield(value = copy(x = x - 1, y = y + 1))
    }

    fun enclosedTo(other: Point): Point = when {
        x == other.x && y == other.y -> this

        x == other.x && y < other.y -> copy(y = y + 1)
        y == other.y && x < other.x -> copy(x = x + 1)

        y < other.y && x < other.x -> copy(y = y + 1, x = x + 1)

        x == other.x -> copy(y = y - 1)
        y == other.y -> copy(x = x - 1)

        y < other.y -> copy(y = y + 1, x = x - 1)
        x < other.x -> copy(y = y - 1, x = x + 1)

        else -> copy(y = y - 1, x = x - 1)
    }

    fun manhattanDistanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)

    companion object {
        val COMPARATOR: Comparator<Point> = compareBy({ it.y }, { it.x })
    }
}