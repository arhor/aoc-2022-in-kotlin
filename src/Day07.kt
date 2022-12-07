fun main() {
    val input = readInput {}.let(::buildFilesystemTree)

    println("Part 1: " + solvePuzzle1(input))
    println("Part 2: " + solvePuzzle2(input))
}

private fun buildFilesystemTree(input: List<String>): Node.Dir {
    val root = Node.Dir("/", null)
    var curr: Node.Dir? = root
    for (line in input) {
        val result = command.find(line)
        if (result != null) {
            if (result.groupValues[1] == "ls") {
                continue
            } else {
                curr = when (val dirName = result.groups["dirName"]!!.value) {
                    "/" -> {
                        root
                    }

                    ".." -> {
                        curr?.prev
                    }

                    else -> {
                        Node.Dir(dirName, curr).also { curr?.next?.add(it) }
                    }
                }
            }
            continue
        }

        val info = fileinf.find(line)
        if (info != null) {
            curr?.next?.add(
                Node.File(
                    name = info.groups["name"]!!.value,
                    size = info.groups["size"]!!.value.toLong(),
                    prev = curr
                )
            )
        }
    }
    return root
}

private fun solvePuzzle1(dir: Node.Dir): Long {
    return (dir.size.takeIf { it <= 100_000 } ?: 0) + dir.next.filterIsInstance<Node.Dir>().sumOf { solvePuzzle1(it) }
}

private fun solvePuzzle2(root: Node.Dir): Long {
    val rootSize = root.size
    return traverse2(root).sortedBy { it.size }.first { TOTAL - (rootSize - it.size) >= NEEDS }.size
}

private val command = Regex("\\$ (ls)|(cd (?<dirName>/|..|[a-zA-Z0-9]+))")
private val fileinf = Regex("(?<size>[0-9]+) (?<name>[a-zA-Z0-9.]+)")

private const val TOTAL = 70_000_000L
private const val NEEDS = 30_000_000L

private sealed interface Node {
    val name: String
    val size: Long
    val prev: Dir?

    data class File(override val name: String, override val size: Long, override val prev: Dir?) : Node

    class Dir(override val name: String, override val prev: Dir?) : Node {
        val next = ArrayList<Node>()
        override val size: Long get() = next.sumOf { it.size }
    }
}

private fun traverse2(dir: Node.Dir): List<Node.Dir> {
    return dir.next.filterIsInstance<Node.Dir>().flatMap { traverse2(it) } + dir
}
