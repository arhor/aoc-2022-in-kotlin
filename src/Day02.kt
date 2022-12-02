fun main() {
    fun part1(input: Sequence<String>) = solvePuzzle(
        input, resultTable = mapOf(
            "AX" to 3 + 1,
            "AY" to 6 + 2,
            "AZ" to 0 + 3,

            "BX" to 0 + 1,
            "BY" to 3 + 2,
            "BZ" to 6 + 3,

            "CX" to 6 + 1,
            "CY" to 0 + 2,
            "CZ" to 3 + 3,
        )
    )

    fun part2(input: Sequence<String>) = solvePuzzle(
        input, resultTable = mapOf(
            "AX" to 0 + 3,
            "AY" to 3 + 1,
            "AZ" to 6 + 2,

            "BX" to 0 + 1,
            "BY" to 3 + 2,
            "BZ" to 6 + 3,

            "CX" to 0 + 2,
            "CY" to 3 + 3,
            "CZ" to 6 + 1,
        )
    )

    println(readInput("Day02", ::part1))
    println(readInput("Day02", ::part2))
}

private fun solvePuzzle(input: Sequence<String>, resultTable: Map<String, Int>): Int {
    return input.map { it.replace(" ", "") }.mapNotNull(resultTable::get).sum()
}
