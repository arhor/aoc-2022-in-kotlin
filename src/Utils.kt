import java.io.File

fun <T> readInput(name: String, block: (Sequence<String>) -> T) =
    File("src", "$name.txt").useLines(block = block)
