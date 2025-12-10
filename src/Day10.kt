
fun main() {

    fun part1(input: List<String>): Long {

        return input.sumOf { line ->
            val machine = parseMachine(line, false)
            solveMachine(machine, true)
        }

    }

    fun part2(input: List<String>): Long {
        val range = input.size
        return input.sumOf { line ->
            val machine = parseMachine(line, true)
            solveMachine(machine, false, range)
        }

    }

//    part1(readInput("Day10_test")).println()
//    part1(readInput("Day10")).println() // 488
//    part2(readInput("Day10_test")).println()
    part2(readInput("Day10")).println() // 18771

}