
fun main() {

    fun part1(input: List<String>): Long {

        var ans = 0L

        input.forEach { bank ->
            ans += getMaxJoltage(bank)
        }

        return ans

    }

    fun part2(input: List<String>): Long {

        var ans = 0L

        input.forEach { bank ->
            ans += getMaxJoltage(bank, 12)
        }

        return ans

    }

//    part1(readInput("Day03_test")).println()
//    part1(readInput("Day03")).println()
//    part2(readInput("Day03_test")).println()
    part2(readInput("Day03")).println()

}