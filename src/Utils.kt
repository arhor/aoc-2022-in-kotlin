import java.io.File

fun readInput(ref: () -> Unit): List<String> {
    return ref.javaClass.name.substringBefore("Kt$").let { File("src", "$it.txt") }.readLines()
}

fun <T> flattenTree(item: T, children: T.() -> Sequence<T>): Sequence<T> {
    return item.children().flatMap { flattenTree(it, children) } + item
}