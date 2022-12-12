fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle(input)}")
}

private fun solvePuzzle(list: List<String>): ULong {
    val monkeys = list.split("").map(::Monkey)
    val factor = lcm(monkeys.map { it.divisor.toInt() }.toIntArray())

    fun tick() {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                val newItem = monkey.worry(item % factor)
//                newItem /= 3
                val newMonkey = monkey.whereToThrow(newItem)
                monkeys[newMonkey].items.add(newItem)
            }
            monkey.items.clear()
        }
    }

    repeat(10_000) {
        tick()
    }

    val (a, b) = monkeys.map { it.inspections }.sorted().takeLast(2).map { it.toULong() }

    return a * b
}

private class Monkey(info: List<String>) {
    val items: MutableList<Long>
    val worry: (Long) -> Long
    val divisor: Long
    val onSuccess: Int
    val onFailure: Int

    var inspections = 0
        private set

    init {
        items = info[1].substringAfter("Starting items: ").split(", ").map(String::toLong).toMutableList()
        worry = info[2].substringAfter("Operation: new = ").split(" ").let { (a, b, c) ->
            val operator: (Long, Long) -> Long = when (b) {
                "+" -> Long::plus
                "-" -> Long::minus
                "*" -> Long::times
                "/" -> Long::div
                else -> throw IllegalStateException("Unsupported operator: $b")
            }
            return@let { item ->
                inspections++
                val one = if (a == "old") item else a.toLong()
                val two = if (c == "old") item else c.toLong()
                operator(one, two)
            }
        }

        divisor = info[3].substringAfter("divisible by ").toLong()
        onSuccess = info[4].substringAfter("If true: throw to monkey ").toInt()
        onFailure = info[5].substringAfter("If false: throw to monkey ").toInt()
    }

    fun whereToThrow(item: Long): Int = if (item % divisor == 0L) onSuccess else onFailure
}
