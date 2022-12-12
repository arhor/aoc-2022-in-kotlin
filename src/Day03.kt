fun main() {
    val input = readInput {}

    println("Part 1: " + solvePuzzle1(input))
    println("Part 2: " + solvePuzzle2(input))
}

private fun solvePuzzle1(input: List<String>): Int {
    return input.sumOf { line ->
        val l = line.substring(0, line.length / 2)
        val r = line.substring(line.length / 2, line.length)
        ALPHABET.indexOf(l.first { it in r })
    }
}

private fun solvePuzzle2(input: List<String>): Int {
    return input.chunked(3).sumOf { (s1, s2, s3) ->
        ALPHABET.indexOf(s1.first { it in s2 && it in s3 })
    }
}
