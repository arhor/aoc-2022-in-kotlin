fun main() {
    val input = readInput {}.first()

    println("Part 1: " + solvePuzzle(input, markerSize = 4))
    println("Part 2: " + solvePuzzle(input, markerSize = 14))
}

private fun solvePuzzle(input: String, markerSize: Int): Int {
    return input.windowed(markerSize).indexOfFirst(::stringWithoutDuplicates) + markerSize
}

private fun stringWithoutDuplicates(string: String): Boolean {
    return string.chars().distinct().count().compareTo(string.length) == 0
}
