fun main() {
    val root = readInput {}.let(::buildFilesystemTree)
    val size = root.size

    println("Part 1: " + root.allDirs.map { it.size }.filter { it <= 100_000 }.sum())
    println("Part 2: " + root.allDirs.map { it.size }.sorted().first { TOTAL - (size - it) >= NEEDS })
}

private fun buildFilesystemTree(input: List<String>): Node.Dir {
    val root = Node.Dir("/", null)
    var curr = root

    for (line in input) {
        when {
            line.startsWith(COMMAND_LS) -> {
                continue
            }

            line.startsWith(COMMAND_CD) -> {
                curr = when (val dirName = line.substringAfter(COMMAND_CD).trim()) {
                    "/"  -> root
                    ".." -> curr.prev ?: root
                    else -> Node.Dir(dirName, curr).also(curr.next::add)
                }
            }

            else -> {
                val (attr, name) = line.split(" ")
                if (attr != "dir") {
                    curr.next.add(
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

private const val COMMAND_LS = "$ ls"
private const val COMMAND_CD = "$ cd"

private const val TOTAL = 70_000_000L
private const val NEEDS = 30_000_000L

private sealed interface Node {
    val name: String
    val size: Long
    val prev: Dir?

    data class File(override val name: String, override val size: Long, override val prev: Dir) : Node

    data class Dir(override val name: String, override val prev: Dir? = null) : Node {
        val next = ArrayList<Node>()
        val allDirs: Sequence<Dir> get() = flattenTree(this) { next.asSequence().filterIsInstance<Dir>() }
        override val size: Long get() = next.sumOf { it.size }
    }
}
