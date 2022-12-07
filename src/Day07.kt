fun main() {
    val input = readInput {}.let(::buildFilesystemTree)

    println("Part 1: " + solvePuzzle1(input))
    println("Part 2: " + solvePuzzle2(input))
}

private fun buildFilesystemTree(input: List<String>): Node.Dir {
    val root = Node.Dir("/", null)
    var curr: Node.Dir? = root

    for (line in input) {
        when {
            line.startsWith(COMMAND_LS) -> {
                continue
            }

            line.startsWith(COMMAND_CD) -> {
                curr = when (val dirName = line.substringAfter(COMMAND_CD).trim()) {
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

            else -> {
                val (attr, name) = line.split(" ")
                if (attr != "dir") {
                    curr?.next?.add(
                        Node.File(
                            name = name,
                            size = attr.toLong(),
                            prev = curr
                        )
                    )
                }
            }
        }
    }
    return root
}

private fun solvePuzzle1(dir: Node.Dir): Long {
    return (dir.size.takeIf { it <= 100_000 } ?: 0) + dir.next.filterIsInstance<Node.Dir>().sumOf { solvePuzzle1(it) }
}

private fun solvePuzzle2(root: Node.Dir): Long {
    val rootSize = root.size
    return flattenTree(root) { it.next.filterIsInstance<Node.Dir>() }
        .map { it.size }
        .sorted()
        .first { TOTAL - (rootSize - it) >= NEEDS }
}

private const val COMMAND_LS = "$ ls"
private const val COMMAND_CD = "$ cd"

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
