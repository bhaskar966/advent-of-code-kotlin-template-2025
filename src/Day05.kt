fun main() {

    fun part1(input: List<String>): Int {
        val blankIdx = input.indexOfFirst { it.isBlank() }

        val freshRanges = input.subList(0, blankIdx).map { line ->
            val (start, end) = line.split('-').map { it.toLong() }
            start..end
        }

        val availableId = input.subList(blankIdx + 1, input.size).map { it.toLong() }

        return availableId.count { id ->
            freshRanges.any { range ->
                id in range
            }
        }
    }

    fun part2(input: List<String>): Long {
        val blankIdx = input.indexOfFirst { it.isBlank() }

        val freshRanges = input.subList(0, blankIdx).map { line ->
            val (start, end) = line.split('-').map { it.toLong() }
            start..end
        }

        return countUniqueIds(freshRanges)
    }


//    part1(input = readInput("Day05_test")).println()
//    part1(input = readInput("Day05")).println()
//    part2(input = readInput("Day05_test")).println()
    part2(input = readInput("Day05")).println()
}