fun main() {
    val input = readInput {}
    val (signal, pixels) = solvePuzzle(input)

    println("Part 1: $signal")
    println("Part 2: $pixels")
}

private const val DISPLAY_WIDTH = 40
private const val DISPLAY_HEIGHT = 6

private fun solvePuzzle(input: List<String>): Pair<Int, String> {
    var x = 1
    var c = 0
    var signal = 0
    var sprite = listOf(0, 1, 2)
    val pixels = List(DISPLAY_HEIGHT) { CharArray(DISPLAY_WIDTH) { '.' } }

    fun tick(times: Int) = repeat(times) {
        val i = c % DISPLAY_WIDTH
        val j = c / DISPLAY_WIDTH

        if (i in sprite) {
            pixels[j][i] = '#'
        }

        if ((++c - 20) % DISPLAY_WIDTH == 0) {
            signal += c * x
        }
    }
    for (line in input) {
        when (line.substringBefore(" ")) {
            "noop" -> {
                tick(1)
            }
            "addx" -> {
                tick(2)
                x += line.substringAfter(" ").let(String::toInt)
                sprite = listOf(x - 1, x, x + 1)
            }
        }
    }
    return signal to pixels.joinToString(separator = "\n", prefix = "\n") { it.concatToString() }
}
