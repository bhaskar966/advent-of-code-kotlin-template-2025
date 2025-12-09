import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Long {

        val points = input.map { line ->
            val (col, row) = line.split(',').map { it.toInt() }
            Point2D(col, row)
        }

        return points.getLargestArea()
    }

    fun part2(input: List<String>): Long {

        val points = input.map { line -> val (col, row) = line.split(',').map { it.toInt() }; Point2D(col, row) }
        return getLargestAreaInPolygon(points)

    }



//    part1(readInput("Day09_test")).println()
//    part1(readInput("Day09")).println()
//    part2(readInput("Day09_test")).println()
    part2(readInput("Day09")).println()

}