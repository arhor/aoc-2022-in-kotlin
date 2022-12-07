import java.io.File

fun readInput(ref: () -> Unit): List<String> {
    return ref.javaClass.name.substringBefore("Kt$").let { File("src", "$it.txt") }.readLines()
}

fun <T> flattenTree(item: T, extract: (T) -> List<T>): List<T> {
    return extract(item).flatMap { flattenTree(it, extract) } + item
}