import java.io.File
import kotlin.math.absoluteValue

fun readInput(ref: () -> Unit): List<String> {
    return ref.javaClass.name.substringBefore("Kt$").let { File("src", "$it.txt") }.readLines()
}

fun <T> flattenTree(item: T, children: T.() -> Sequence<T>): Sequence<T> {
    return sequenceOf(item) + item.children().flatMap { flattenTree(it, children) }
}

fun <T> List<List<T>>.iterateToEdges(x: Int, y: Int): Sequence<List<T>> {
    val data = this
    return sequence {
        yield(value = data[y].slice(0 until x).reversed())
        yield(value = data[y].slice((x + 1) until data[y].size))
        yield(value = (0 until y).map { data[it][x] }.reversed())
        yield(value = ((y + 1) until data.size).map { data[it][x] })
    }
}

inline fun <T> Iterable<T>.countUntil(isValid: (T) -> Boolean): Int {
    var count = 0
    for (item in this) {
        count++
        if (!isValid(item)) break
    }
    return count
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {

    override fun compareTo(other: Point): Int = comparator.compare(this, other)

    fun adjacentPoints(self: Boolean = true, diagonal: Boolean = true) = sequence {
        if (self) yield(value = this@Point)
        yield(value = copy(y = y + 1))
        if (diagonal) yield(value = copy(x = x + 1, y = y + 1))
        yield(value = copy(x = x + 1))
        if (diagonal) yield(value = copy(x = x + 1, y = y - 1))
        yield(value = copy(y = y - 1))
        if (diagonal) yield(value = copy(x = x - 1, y = y - 1))
        yield(value = copy(x = x - 1))
        if (diagonal) yield(value = copy(x = x - 1, y = y + 1))
    }

    fun enclosedTo(other: Point) = when {
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

    companion object {
        val comparator = compareBy(Point::y, Point::x)
    }
}

fun <T> List<T>.split(separator: T): List<List<T>> =
    fold(initial = mutableListOf(ArrayList<T>())) { result, element ->
        result.also {
            if (element == separator) {
                it.add(ArrayList())
            } else {
                it.last().add(element)
            }
        }
    }

tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

fun gcd(arr: IntArray): Int = arr.reduce { a, b -> gcd(a, b) }

fun lcm(a: Int, b: Int): Int = (a * b).absoluteValue / gcd(a, b)

fun lcm(arr: IntArray): Int = arr.reduce { a, b -> lcm(a, b) }

const val ALPHABET = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
