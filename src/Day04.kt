fun main() {
    val input = readInput("Day04").map { line ->
        line.split(",")
            .map { pair -> pair.split("-").map(String::toInt) }
            .map { (min, max) -> Range(min, max) }
    }

    println("Part 1: " + solvePuzzle(input))
    println("Part 2: " + solvePuzzle(input, completelyOverlapping = false))
}

private fun solvePuzzle(input: List<List<Range>>, completelyOverlapping: Boolean = true): Int {
    return input.count { (one, two) -> one.overlaps(two, completely = completelyOverlapping) }
}

private data class Range(val min: Int, val max: Int) {
    fun overlaps(that: Range, completely: Boolean = false): Boolean = if (completely) {
        that.min >= this.min && that.max <= this.max || that.min <= this.min && that.max >= this.max
    } else {
        that.min >= this.min && that.min <= this.max || that.min <= this.min && that.max >= this.min
    }
}
