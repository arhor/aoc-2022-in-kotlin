fun main() {
    fun part1(input: Sequence<String>) = solvePuzzle(input)
    fun part2(input: Sequence<String>) = solvePuzzle(input, numberOfItemsToTake = 3)

    println(readInput("Day01", ::part1))
    println(readInput("Day01", ::part2))
}

private fun solvePuzzle(input: Sequence<String>, numberOfItemsToTake: Int = 1): Int =
    input.fold(initial = mutableListOf(0)) { list, line ->
        list.apply {
            if (line.isEmpty()) {
                list.add(0)
            } else {
                list[lastIndex] = list.last() + line.toInt()
            }
        }
    }.sortedDescending().take(numberOfItemsToTake).sum()
