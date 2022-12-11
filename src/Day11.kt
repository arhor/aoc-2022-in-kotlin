fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzle(input)}")
}

private fun solvePuzzle(list: List<String>): ULong {
    val monkeys = list.split("").map(::Monkey)
    val factor = lcm(monkeys.map { it.divisor.toInt() }.toIntArray())

    println("factor: $factor")

    fun tick() {
        for (monkey in monkeys) {
//            println("Monkey ${monkey.index}:")
            for (item in monkey.items) {
//                println("\tMonkey inspects an item with a worry level of $item.")
                val newItem = monkey.worry(item % factor)
//                println("\tWorry level became $newItem.")
//                newItem /= 3
//                println("\tMonkey gets bored with item. Worry level is divided by 3 to $newItem.")
                val newMonkey = monkey.rules(newItem)
//                println("\tItem with worry level $newItem is thrown to monkey $newMonkey.")
                monkeys[newMonkey].items.add(newItem)
            }
            monkey.items.clear()
        }
        println("\n============================================================\n")
    }

    repeat(10_000) {
        println("round: ${it + 1}")
        tick()
        monkeys.forEach(::println)
    }

    val (a, b) =  monkeys.map { it.inspections }.sorted().takeLast(2).map { it.toULong() }

    println("a: $a")
    println("b: $b")

    return a * b
}

private class Monkey(info: List<String>) {
    val index: Int
    val items: MutableList<Long>
    val worry: (Long) -> Long
    val rules: (Long) -> Int

    val divisor: Long
    val onSuccess: Int
    val onFailure: Int

    var inspections = 0
        private set

    init {
        index = info[0].substringAfter("Monkey ").substringBefore(":").toInt()
        items = info[1].substringAfter("Starting items: ").split(", ").map(String::toLong).toMutableList()

        val (a, b, c) = info[2].substringAfter("Operation: new = ").split(" ")
        val operator: (Long, Long) -> Long = when (b) {
            "+" -> Long::plus
            "-" -> Long::minus
            "*" -> Long::times
            "/" -> Long::div
            else -> throw IllegalStateException("Unsupported operator: $b")
        }

        worry = { item ->
            val one = if ((a == "old")) item else a.toLong()
            val two = if ((c == "old")) item else c.toLong()

            inspections++

            operator(one, two)
        }

        divisor = info[3].substringAfter("divisible by ").toLong()
        onSuccess = info[4].substringAfter("If true: throw to monkey ").toInt()
        onFailure = info[5].substringAfter("If false: throw to monkey ").toInt()

        rules = { item ->
            if ((item % divisor) == 0L) {
                onSuccess
            } else {
                onFailure
            }
        }
    }

    override fun toString(): String {
        return "Monkey(index=$index, items=$items, worry='$worry', rules=$rules, inspections=$inspections)"
    }
}
