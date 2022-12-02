fun main() {
    fun part1(input: Sequence<String>) = solvePuzzle(
        input, resultTable = hashMapOf(
            "AX" to Shape.ROCK     + Round.DRAW,
            "AY" to Shape.PAPER    + Round.WIN,
            "AZ" to Shape.SCISSORS + Round.LOOSE,

            "BX" to Shape.ROCK     + Round.LOOSE,
            "BY" to Shape.PAPER    + Round.DRAW,
            "BZ" to Shape.SCISSORS + Round.WIN,

            "CX" to Shape.ROCK     + Round.WIN,
            "CY" to Shape.PAPER    + Round.LOOSE,
            "CZ" to Shape.SCISSORS + Round.DRAW,
        )
    )

    fun part2(input: Sequence<String>) = solvePuzzle(
        input, resultTable = hashMapOf(
            "AX" to Round.LOOSE + Shape.SCISSORS,
            "AY" to Round.DRAW  + Shape.ROCK,
            "AZ" to Round.WIN   + Shape.PAPER,

            "BX" to Round.LOOSE + Shape.ROCK,
            "BY" to Round.DRAW  + Shape.PAPER,
            "BZ" to Round.WIN   + Shape.SCISSORS,

            "CX" to Round.LOOSE + Shape.PAPER,
            "CY" to Round.DRAW  + Shape.SCISSORS,
            "CZ" to Round.WIN   + Shape.ROCK,
        )
    )

    println(readInput("Day02", ::part1))
    println(readInput("Day02", ::part2))
}

private fun solvePuzzle(input: Sequence<String>, resultTable: Map<String, Int>): Int {
    return input.map { it.replace(" ", "") }.mapNotNull(resultTable::get).sum()
}

private interface Scored {
    val score: Int
    operator fun plus(that: Scored) = this.score + that.score
}

private enum class Round(override val score: Int) : Scored { LOOSE(0), DRAW(3), WIN(6) }
private enum class Shape(override val score: Int) : Scored { ROCK(1), PAPER(2), SCISSORS(3) }
