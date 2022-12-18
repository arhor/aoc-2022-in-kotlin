import java.io.File
import kotlin.math.absoluteValue

fun readInput(ref: () -> Unit): List<String> {
    return ref.javaClass.name.substringBefore("Kt$").let { File("src", "$it.txt") }.readLines()
}

fun <T> flattenTree(item: T, children: T.() -> Sequence<T>): Sequence<T> {
    return sequenceOf(item) + item.children().flatMap { flattenTree(it, children) }
}

inline fun <T> Iterable<T>.countUntil(isValid: (T) -> Boolean): Int {
    var count = 0
    for (item in this) {
        count++
        if (!isValid(item)) break
    }
    return count
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

inline fun StringBuilder.extractTo(consumer: (String) -> Unit) {
    if (this.isNotEmpty()) {
        val value = this.toString()
        consumer(value)
        this.clear()
    }
}

fun IntRange.overlaps(other: IntRange) = maxOf(first, other.first) <= minOf(last, other.last) + 1

fun IntRange.merge(other: IntRange) = minOf(first, other.first)..maxOf(last, other.last)
