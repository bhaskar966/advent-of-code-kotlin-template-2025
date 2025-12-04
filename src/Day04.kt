
fun main() {

    fun part1(input: List<String>): Int {
        var rollsCanBeAccessedCnt = 0
        val totalRows = input.size
        input.forEachIndexed { index, row ->
            rollsCanBeAccessedCnt += getAccessibleRollsEachRow(
                prevRow = if(index != 0) input[index-1] else null,
                currRow = row,
                nextRow = if(index != totalRows-1) input[index+1] else null
            ).size
        }

        return rollsCanBeAccessedCnt
    }

    fun part2(input: List<String>): Int {
        var rollsCanBeAccessedCnt = 0
        var operationalGrid = input
        while (true) {
            val accessibleIndexes = findAllAccessibleRolls(operationalGrid)
            if(accessibleIndexes.values.all { it.isEmpty() }) break

            rollsCanBeAccessedCnt += accessibleIndexes.values.sumOf { it.size }
            operationalGrid = removeAcceptedRolls(operationalGrid, accessibleIndexes)
        }

        return rollsCanBeAccessedCnt
    }

//    part1(readInput("Day04")).println() // 1527
//    part1(readInput("Day04_test")).println()

    part2(readInput("Day04")).println() // 8690
//    part2(readInput("Day04_test")).println()
}