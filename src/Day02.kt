fun main() {
    fun part1(input: Sequence<String>) = solvePuzzle(
        input, resultTable = hashMapOf(
            "A X" to Shape.ROCK     + Round.DRAW,
            "A Y" to Shape.PAPER    + Round.WIN,
            "A Z" to Shape.SCISSORS + Round.LOOSE,

            "B X" to Shape.ROCK     + Round.LOOSE,
            "B Y" to Shape.PAPER    + Round.DRAW,
            "B Z" to Shape.SCISSORS + Round.WIN,

            "C X" to Shape.ROCK     + Round.WIN,
            "C Y" to Shape.PAPER    + Round.LOOSE,
            "C Z" to Shape.SCISSORS + Round.DRAW,
        )
    )

    fun part2(input: Sequence<String>) = solvePuzzle(
        input, resultTable = hashMapOf(
            "A X" to Round.LOOSE + Shape.SCISSORS,
            "A Y" to Round.DRAW  + Shape.ROCK,
            "A Z" to Round.WIN   + Shape.PAPER,

            "B X" to Round.LOOSE + Shape.ROCK,
            "B Y" to Round.DRAW  + Shape.PAPER,
            "B Z" to Round.WIN   + Shape.SCISSORS,

            "C X" to Round.LOOSE + Shape.PAPER,
            "C Y" to Round.DRAW  + Shape.SCISSORS,
            "C Z" to Round.WIN   + Shape.ROCK,
        )
    )

    println(readInput("Day02", ::part1))
    println(readInput("Day02", ::part2))
}

private fun solvePuzzle(input: Sequence<String>, resultTable: Map<String, Int>): Int {
    return input.mapNotNull(resultTable::get).sum()
}

private interface Scored {
    val score: Int
    operator fun plus(that: Scored) = this.score + that.score
}

private enum class Round(override val score: Int) : Scored { LOOSE(0), DRAW(3), WIN(6) }
private enum class Shape(override val score: Int) : Scored { ROCK(1), PAPER(2), SCISSORS(3) }
