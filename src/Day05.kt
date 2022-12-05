import java.util.*

fun main() {
    val input = readInput {}

    println("Part 1: " + solvePuzzle(input))
    println("Part 2: " + solvePuzzle2(input))
}

private val cratePattern = Regex("(\\[([A-Z])])|( {3})")
private val instrPattern = Regex("^move ([0-9]+) from ([0-9]+) to ([0-9]+)$")

private fun solvePuzzle(input: List<String>): String {
    val table = TreeMap<Int, Deque<String>>()

    for (line in input) {
        cratePattern.findAll(line)
            .map { it.groupValues[2] }
            .forEachIndexed { i, crate ->
                if (crate.isNotEmpty()) {
                    table.computeIfAbsent(i + 1) { LinkedList() }.push(crate)
                }
            }
        instrPattern.find(line)?.destructured?.toList()?.map { it.toInt() }?.let { (number, from, to) ->
            repeat(number) {
                table[to]!!.add(table[from]!!.removeLast())
            }
        }
    }
    return table.values.joinToString(separator = "") { it.last }
}

private fun solvePuzzle2(input: List<String>): String {
    val table = TreeMap<Int, LinkedList<String>>()

    for (line in input) {
        cratePattern.findAll(line)
            .map { it.groupValues[2] }
            .forEachIndexed { i, crate ->
                if (crate.isNotEmpty()) {
                    table.computeIfAbsent(i + 1) { LinkedList() }.push(crate)
                }
            }
        instrPattern.find(line)?.destructured?.toList()?.map { it.toInt() }?.let { (number, from, to) ->
            val insertPos = table[to]!!.lastIndex
            repeat(number) {
                table[to]!!.add(insertPos + 1, table[from]!!.removeLast())
            }
        }
    }
    return table.values.joinToString(separator = "") { it.last }
}
