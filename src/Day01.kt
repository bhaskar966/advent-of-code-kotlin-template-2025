import java.lang.Math.floorDiv

fun main() {
    fun part1(input: List<String>): Int {
        val dialSize = 100
        var currPoint = 50
        var zeroCnt = 0
        input.forEach { rotation ->
            val direction = rotation[0]
            val steps = rotation.substring(1).toInt()

            currPoint = when (direction) {
                'L' -> (currPoint - steps).mod(dialSize)
                else -> (currPoint + steps).mod(dialSize)
            }

            if (currPoint == 0) {
                zeroCnt++
            }
        }
        return zeroCnt
    }

    fun part2(input: List<String>): Int {
        val dialSize = 100
        var currPoint = 50
        var zeroCnt = 0

        input.forEach { rotation ->
            val direction = rotation[0]
            val steps = rotation.substring(1).toInt()
            val prevPoint = currPoint

            zeroCnt += when (direction) {
                'L' -> {
                    floorDiv(prevPoint - 1, dialSize) - floorDiv(prevPoint - steps - 1, dialSize)
                }
                else -> {
                    floorDiv(prevPoint + steps, dialSize) - floorDiv(prevPoint, dialSize)
                }
            }

            currPoint = when (direction) {
                'L' -> (prevPoint - steps).mod(dialSize)
                else -> (prevPoint + steps).mod(dialSize)
            }
        }
        return zeroCnt
    }

//    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)
//
//    // Or read a large test input from the `src/Day01_test.txt` file:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
//    part1(input).println()
    part2(input).println()
}
