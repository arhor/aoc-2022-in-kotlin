import java.io.File

fun readInput(ref: () -> Unit): List<String> {
    return ref.javaClass.name.substringBefore("Kt$").let { File("src", "$it.txt") }.readLines()
}