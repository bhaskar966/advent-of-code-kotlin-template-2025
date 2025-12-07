

fun main() {

    fun part1(input: List<String>): Long {
        val numRows = input.size - 1
        val width = input.maxOf { it.length }

        // Find problem boundaries (identify which columns are separators)
        val problemStarts = mutableListOf<Int>()
        var inProblem = false

        for (col in 0 until width) {
            val isAllSpaces = (0..numRows).all { row ->
                input[row].getOrNull(col)?.isWhitespace() ?: true
            }

            if (!isAllSpaces && !inProblem) {
                problemStarts.add(col)
                inProblem = true
            } else if (isAllSpaces) {
                inProblem = false
            }
        }

        // Add end marker
        problemStarts.add(width)

        var total = 0L

        // Process each problem
        for (i in 0 until problemStarts.size - 1) {
            val startCol = problemStarts[i]
            val endCol = problemStarts.getOrNull(i + 1) ?: width

            val operands = mutableListOf<Long>()
            var operator = ' '

            // Read each row within this problem's column range
            for (row in 0 until numRows) {
                val substring = input[row].substring(startCol, minOf(endCol, input[row].length))
                val number = substring.trim()
                if (number.isNotEmpty()) {
                    operands.add(number.toLong())
                }
            }

            // Get operator from last row
            for (col in startCol until minOf(endCol, input.last().length)) {
                val char = input.last()[col]
                if (char in setOf('+', '*')) {
                    operator = char
                    break
                }
            }

            // Calculate result
            if (operands.isNotEmpty() && operator != ' ') {
                val result = if (operator == '+') {
                    operands.sum()
                } else {
                    operands.fold(1L) { acc, n -> acc * n }
                }
                total += result
            }
        }

        return total
    }

    fun part2(input: List<String>): Long {
        val numRows = input.size - 1
        val width = input.maxOf { it.length }

        // Find problem boundaries
        val problemRanges = mutableListOf<IntRange>()
        var problemStart: Int? = null

        for (col in 0 until width) {
            val hasContent = (0 until numRows).any { row ->
                input[row].getOrNull(col)?.isDigit() == true
            }

            if (hasContent && problemStart == null) {
                problemStart = col
            } else if (!hasContent && problemStart != null) {
                problemRanges.add(problemStart until col)
                problemStart = null
            }
        }
        if (problemStart != null) {
            problemRanges.add(problemStart until width)
        }

        var total = 0L

        // Process each problem
        for (range in problemRanges) {
            val numbers = mutableListOf<Long>()
            var operator = ' '

            // Read columns RIGHT-TO-LEFT within this problem
            for (col in range.reversed()) {
                // Read this column top-to-bottom to form a number
                var numberStr = ""
                for (row in 0 until numRows) {
                    val char = input[row].getOrNull(col)
                    if (char != null && char.isDigit()) {
                        numberStr += char
                    }
                }

                if (numberStr.isNotEmpty()) {
                    numbers.add(numberStr.toLong())
                }

                // Get operator from this column
                val opChar = input.last().getOrNull(col)
                if (opChar in setOf('+', '*')) {
                    operator = opChar!!
                }
            }

            // Calculate result
            if (numbers.isNotEmpty() && operator != ' ') {
                val result = if (operator == '+') {
                    numbers.sum()
                } else {
                    numbers.fold(1L) { acc, n -> acc * n }
                }
                total += result
            }
        }

        return total
    }




//    part1(readInput("Day06_test")).println()
//    part1(readInput("Day06")).println()

//    part2(readInput("Day06_test")).println()
    part2(readInput("Day06")).println()

}