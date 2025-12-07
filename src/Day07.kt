
fun main() {

    fun part1(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length

        val (startRow, startCol) = findStart(input)

        var splitCount = 0
        val queue = ArrayDeque<Beam>()
        val visited = mutableSetOf<Beam>()

        queue.add(Beam(startRow + 1, startCol))

        while (queue.isNotEmpty()) {
            val beam = queue.removeFirst()

            if (beam.row !in 0 until rows || beam.col !in 0 until cols) continue

            if (!visited.add(beam)) continue

            val cell = input[beam.row][beam.col]

            when (cell) {
                '.' -> {
                    queue.add(Beam(beam.row + 1, beam.col))
                }
                '^' -> {
                    splitCount++

                    queue.add(Beam(beam.row + 1, beam.col - 1))
                    queue.add(Beam(beam.row + 1, beam.col + 1))
                }
            }
        }

        return splitCount
    }

    fun part2(input: List<String>): Long {
        val rows = input.size
        val cols = input[0].length
        val (startRow, startCol) = findStart(input)

        val memo = mutableMapOf<Beam, Long>()

        fun countPaths(beam: Beam): Long {
            if (!isInBounds(beam.row, beam.col, rows, cols)) {
                return 1L
            }

            if (beam in memo) {
                return memo[beam]!!
            }

            val result = when (input[beam.row][beam.col]) {
                '.' -> {
                    countPaths(Beam(beam.row + 1, beam.col))
                }
                '^' -> {
                    val leftPaths = countPaths(Beam(beam.row + 1, beam.col - 1))
                    val rightPaths = countPaths(Beam(beam.row + 1, beam.col + 1))
                    leftPaths + rightPaths
                }
                else -> 0L
            }

            memo[beam] = result
            return result
        }

        return countPaths(Beam(startRow + 1, startCol))
    }

//    part1(readInput("Day07_test")).println()
//    part1(readInput("Day07")).println() //1656
//    part2(readInput("Day07_test")).println()
    part2(readInput("Day07")).println()

}