import java.util.*

fun main() {
    val input = readInput {}

    println("Part 1: " + solvePuzzle(input, insertReversed = true))
    println("Part 2: " + solvePuzzle(input, insertReversed = false))
}

private val cratePattern = Regex("(\\[([A-Z])])| {3}")
private val instrPattern = Regex("^move ([0-9]+) from ([0-9]+) to ([0-9]+)$")

private fun solvePuzzle(input: List<String>, insertReversed: Boolean): String {
    val table = TreeMap<Int, LinkedList<String>>()

    for (line in input) {
        cratePattern.findAll(line).map { it.groupValues[2] }.forEachIndexed { i, crate ->
            if (crate.isNotEmpty()) {
                table.computeIfAbsent(i + 1) { LinkedList() }.push(crate)
            }
        }
        instrPattern.find(line)?.destructured?.toList()?.map { it.toInt() }?.let { (number, from, to) ->
            val source = table[from]!!
            val target = table[to]!!

            if (insertReversed) {
                repeat(number) {
                    target.add(source.removeLast())
                }
            } else {
                val insertPos = target.lastIndex
                repeat(number) {
                    target.add(insertPos + 1, source.removeLast())
                }
            }
        }
    }
    return table.values.joinToString(separator = "") { it.last }
}
