import java.io.File

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
