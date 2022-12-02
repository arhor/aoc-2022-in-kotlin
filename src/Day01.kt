fun main() {
    val input = readInput("Day01")

    println("Part 1: " + solvePuzzle(input))
    println("Part 2: " + solvePuzzle(input, numberOfItemsToTake = 3))
}

private fun solvePuzzle(input: List<String>, numberOfItemsToTake: Int = 1): Int =
    input.fold(initial = mutableListOf(0)) { list, line ->
        list.apply {
            if (line.isEmpty()) {
                list.add(0)
            } else {
                list[lastIndex] = list.last() + line.toInt()
            }
        }
    }.sortedDescending().take(numberOfItemsToTake).sum()
