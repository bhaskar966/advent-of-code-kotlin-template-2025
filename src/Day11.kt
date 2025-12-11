
fun main() {

    fun part1(input: List<String>) : Long {
        val graph = parseGraph(input)
        val memo = mutableMapOf<PathState, Long>()
        return countPaths(
            graph = graph,
            current = "you",
            target = "out",
            memo = memo
        )
    }

    fun part2(input: List<String>) : Long {
        val graph = parseGraph(input)
        val memo = mutableMapOf<PathState, Long>()
        return countPaths(
            graph = graph,
            current = "svr",
            target = "out",
            memo = memo,
            requiredNodes = setOf("dac", "fft")
        )
    }

//    part1(readInput("Day11_test")).println()
//    part1(readInput("Day11")).println() // 690
//    part2(readInput("Day11_test2")).println()
    part2(readInput("Day11")).println() // 557332758684000

}