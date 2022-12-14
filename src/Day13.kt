import java.util.*

fun main() {
    val input = readInput {}

    println("Part 1: ${solvePuzzleByUma(input)}")
}

private fun solvePuzzleByUma(input: List<String>): Int {
    var sum = 0
    for ((index, packets) in input.split("").withIndex()) {
        val one = parse(packets[0])
        val two = parse(packets[1])

        val result = checkOrder(one, two)

        if (result == true) {
            sum += (index + 1)
        }
    }
    return sum
}

private fun checkOrder(left: Any?, right: Any?): Boolean? {
    when {
        left is Int && right is Int -> return when {
            left < right -> true
            left > right -> false
            else -> null
        }

        left is List<*> && right is List<*> -> {
            var i = 0
            while (true) {
                when {
                    i  > left.lastIndex && i <= right.lastIndex -> return true
                    i <= left.lastIndex && i  > right.lastIndex -> return false
                    i  > left.lastIndex && i  > right.lastIndex -> return null
                }
                checkOrder(left[i], right[i])?.let {
                    return it
                }
                i++
            }
        }

        left  is Int -> return checkOrder(listOf(left), right)
        right is Int -> return checkOrder(left, listOf(right))
        else         -> return null
    }
}

private fun parse(input: String): List<Any> {
    val stack = ArrayDeque<ArrayList<Any>>()
    val value = StringBuilder()

    var last = emptyList<Any>()

    for (char in input) {
        when (char) {
            '[' -> {
                val nextList = ArrayList<Any>()
                stack.peek()?.add(nextList)
                stack.push(nextList)
            }

            ']' -> {
                last = stack.pop()
                value.extractTo { last.add(it.toInt()) }
            }

            ',' -> {
                value.extractTo { stack.peek().add(it.toInt()) }
            }

            else -> value.append(char)
        }
    }

    return last
}
