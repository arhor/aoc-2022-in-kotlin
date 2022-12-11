fun main() {
    val input = readInput {}

    println("Part 1: " + solvePuzzle(input))
    println("Part 2: " + solvePuzzle(input, numberOfItemsToTake = 3))
}

private fun solvePuzzle(list: List<String>, numberOfItemsToTake: Int = 1): Int =
    list.split("")
        .map { it.map(String::toInt).sum() }
        .sortedDescending()
        .take(numberOfItemsToTake)
        .sum()
