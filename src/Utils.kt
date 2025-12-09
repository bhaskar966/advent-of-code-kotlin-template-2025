import java.math.BigInteger
import java.security.MessageDigest
import kotlin.collections.forEachIndexed
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun readInputAndParsePair(name: String) = Path("src/$name.txt").readText().split(',').map { str ->
    val parts = str.split('-')
    Pair(parts[0], parts[1])
}

// Day 3
fun getMaxJoltage(bank: String, batteryCount: Int = 2): Long {

    val n = bank.length
    val skip = n - batteryCount

    if(batteryCount > n) return 0L

    val result = StringBuilder()
    var currentIndex = 0
    var remainingToSkip = skip

    for(picked in 0 until batteryCount) {
        val remainingToPick = batteryCount - picked
        val searchEnd = minOf(currentIndex + remainingToSkip + 1, n - remainingToPick + 1)

        var maxDigit = '0'
        var maxIndex = currentIndex

        for(i in currentIndex until searchEnd) {
            if(bank[i] > maxDigit) {
                maxDigit = bank[i]
                maxIndex = i
            }
        }

        result.append(maxDigit)
        remainingToSkip -= (maxIndex - currentIndex)
        currentIndex = maxIndex + 1
    }

    return result.toString().toLong()

}

// Day 4
fun getAccessibleRollsEachRow(
    prevRow: String? = null,
    currRow: String,
    nextRow: String? = null,
): List<Int> {

    val accessibleRollsIndex : MutableList<Int> = mutableListOf()
    val totalPointsInRow = currRow.length
    currRow.forEachIndexed { index, character ->
        if(character == '@') {
            var surroundingRollsCurrRow = 0
            if(index != 0 && currRow[index-1] == '@') surroundingRollsCurrRow++
            if(index != totalPointsInRow - 1 && currRow[index+1] == '@') surroundingRollsCurrRow++
            when {
                prevRow == null -> {
                    var surroundingRollsNextRow = 0
                    for (i in maxOf(index-1, 0) .. minOf(index+1, totalPointsInRow-1)){
                        if(nextRow!![i] == '@') surroundingRollsNextRow++
                    }
                    if(surroundingRollsNextRow + surroundingRollsCurrRow < 4) accessibleRollsIndex.add(index)
                }
                nextRow == null -> {
                    var surroundingRollsPrevRow = 0
                    for (i in maxOf(index-1, 0) .. minOf(index+1, totalPointsInRow-1)){
                        if(prevRow[i] == '@') surroundingRollsPrevRow++
                    }
                    if(surroundingRollsPrevRow + surroundingRollsCurrRow < 4) accessibleRollsIndex.add(index)

                }
                else -> {
                    var surroundingRollsBothRows = 0
                    for (i in maxOf(index-1, 0)..minOf(index+1, totalPointsInRow-1)){
                        if(prevRow[i] == '@') surroundingRollsBothRows++
                        if(nextRow[i] == '@') surroundingRollsBothRows++
                    }
                    if(surroundingRollsBothRows + surroundingRollsCurrRow < 4) accessibleRollsIndex.add(index)
                }
            }
        }
    }

    return accessibleRollsIndex
}

fun findAllAccessibleRolls(grid: List<String>): Map<Int, List<Int>> {
    val accessibleIndexesInGrid = mutableMapOf<Int, List<Int>>()

    grid.forEachIndexed { rowIndex, rolls ->
        accessibleIndexesInGrid[rowIndex] = getAccessibleRollsEachRow(
            prevRow = if(rowIndex != 0) grid[rowIndex-1] else null,
            currRow = rolls,
            nextRow = if(rowIndex != grid.size-1) grid[rowIndex+1] else null,
        )
    }

    return accessibleIndexesInGrid
}

fun removeAcceptedRolls(grid: List<String>, accessibleIndexes: Map<Int, List<Int>>): List<String> {
    val mutableGrid = grid.toMutableList()
    accessibleIndexes.forEach { (row, rolls) ->
        val rowChars = mutableGrid[row].toCharArray()
        rolls.forEach {
            rowChars[it] = '.'
        }
        mutableGrid[row] = String(rowChars)
    }
    return mutableGrid
}

// Day 5
fun countUniqueIds(ranges: List<LongRange>): Long {

    val sorted = ranges.sortedBy { it.first }

    var totalCount = 0L
    var currentStart = sorted[0].first
    var currentEnd = sorted[0].last

    for(i in 1 until sorted.size) {
        val range = sorted[i]

        if(range.first <= currentEnd + 1) {
            currentEnd = maxOf(currentEnd, range.last)
        } else {
            totalCount += (currentEnd - currentStart + 1)
            currentStart = range.first
            currentEnd = range.last
        }
    }

    totalCount += (currentEnd - currentStart + 1)

    return totalCount

}

// Day 7
data class Beam(val row: Int, val col: Int)

fun findStart(input: List<String>): Pair<Int, Int> {
    for(r in input.indices) {
        for (c in input[r].indices) {
            if(input[r][c] == 'S') {
                return r to c
            }
        }
    }
    return 0 to 0
}

fun isInBounds(row: Int, col: Int, rows: Int, cols: Int): Boolean {
    return row in 0 until rows && col in 0 until cols
}

// Day 8
data class Point3d(val x: Long, val y: Long, val z: Long)

data class Edge(val u: Int, val v: Int, val dist2: Long)

fun parsePoints(lines: List<String>): List<Point3d> = lines
    .filter { it.isNotBlank() }
    .map { line ->
        val (x,y,z) = line.split(',').map { it.trim().toLong() }
        Point3d(x,y,z)
    }

fun squaredDistance(a: Point3d, b: Point3d): Long {
    val dx = a.x - b.x
    val dy = a.y - b.y
    val dz = a.z - b.z
    return dx * dx + dy * dy + dz * dz
}

class DisjointSet(n: Int) {
    private val parent = IntArray(n) { it }
    private val size = IntArray(n) { 1 }

    fun find(x: Int): Int {
        if(parent[x] != x) parent[x] = find(parent[x])
        return parent[x]
    }

    fun union(x: Int, y: Int) {
        var rx = find(x)
        var ry = find(y)
        if(rx == ry) return

        if (size[rx] < size[ry]) {
            val tmp = rx
            rx = ry
            ry = tmp
        }
        parent[ry] = rx
        size[rx] += size[ry]
    }

    fun componentSize(): List<Int> {
        val rootCounts = mutableMapOf<Int, Int>()
        for(i in parent.indices) {
            val r = find(i)
            rootCounts[r] = (rootCounts[r] ?: 0) + 1
        }
        return rootCounts.values.toList()
    }

    fun countComponents(): Int {
        return parent.indices.count { find(it) == it }
    }
}

fun buildAllEdges(points: List<Point3d>): List<Edge> {

    val n = points.size
    val edges = ArrayList<Edge>(n * (n - 1) / 2)

    for(i in 0 until n) {
        for (j in i+1 until n) {
            val d2 = squaredDistance(points[i],points[j])
            edges.add(Edge(i, j, d2))
        }
    }

    return edges.sortedBy { it.dist2 }

}

// Day 9
data class Point2D(val col: Int, val row: Int)

fun List<Point2D>.getLargestArea(
    isValid: (Point2D, Point2D) -> Boolean = { _, _ -> true }
): Long {
    var largestArea = 0L
    val n = this.size

    for (i in 0 until n) {
        for (j in i + 1 until n) {
            if(isValid(this[i], this[j])){
                val width = abs(this[i].col - this[j].col) + 1
                val height = abs(this[i].row - this[j].row) + 1

                largestArea = maxOf(largestArea, width.toLong() * height.toLong())
            }
        }
    }

    return largestArea
}

val pointInPolygonCache = mutableMapOf<Point2D, Boolean>()

fun isPointValidCached(point: Point2D, polygon: List<Point2D>): Boolean {
    return pointInPolygonCache.getOrPut(point) {
        point in polygon || isPointInPolygon(point, polygon) || isOnPolygonEdge(point, polygon)
    }
}

fun isOnPolygonEdge(point: Point2D, polygon: List<Point2D>): Boolean {
    val n = polygon.size
    for (i in 0 until n) {
        val p1 = polygon[i]
        val p2 = polygon[(i + 1) % n]

        // Check if point is on the line segment between p1 and p2
        if (isPointOnSegment(point, p1, p2)) return true
    }
    return false
}

fun isPointOnSegment(point: Point2D, p1: Point2D, p2: Point2D): Boolean {
    val minCol = minOf(p1.col, p2.col)
    val maxCol = maxOf(p1.col, p2.col)
    val minRow = minOf(p1.row, p2.row)
    val maxRow = maxOf(p1.row, p2.row)

    if (point.col !in minCol..maxCol || point.row !in minRow..maxRow) return false

    val cross = (point.row - p1.row) * (p2.col - p1.col) - (point.col - p1.col) * (p2.row - p1.row)
    return cross == 0
}


fun isPointInPolygon(point: Point2D, polygon: List<Point2D>): Boolean {
    var inside = false
    val n = polygon.size

    var j = n - 1
    for (i in 0 until n) {
        val xi = polygon[i].col
        val yi = polygon[i].row
        val xj = polygon[j].col
        val yj = polygon[j].row

        val intersect = ((yi > point.row) != (yj > point.row)) &&
                (point.col < (xj - xi) * (point.row - yi) / (yj - yi) + xi)

        if (intersect) inside = !inside
        j = i
    }

    return inside
}

fun getLargestAreaInPolygon(points: List<Point2D>): Long {
    if (points.isEmpty()) return 0L

    // 1. Coordinate Compression
    val uniqueCols = points.map { it.col }.distinct().sorted()
    val uniqueRows = points.map { it.row }.distinct().sorted()
    val colMap = uniqueCols.withIndex().associate { (i, v) -> v to i }
    val rowMap = uniqueRows.withIndex().associate { (i, v) -> v to i }

    val compressedGridCols = uniqueCols.size
    val compressedGridRows = uniqueRows.size

    val validityGrid = Array(compressedGridRows) { IntArray(compressedGridCols) }
    pointInPolygonCache.clear()

    for (r in 0 until compressedGridRows) {
        for (c in 0 until compressedGridCols) {

            val sampleCol = if (c + 1 < uniqueCols.size) (uniqueCols[c] + uniqueCols[c + 1]) / 2 else uniqueCols[c]
            val sampleRow = if (r + 1 < uniqueRows.size) (uniqueRows[r] + uniqueRows[r + 1]) / 2 else uniqueRows[r]

            val currentPoint = Point2D(sampleCol, sampleRow)

            if (!isPointValidCached(currentPoint, points)) {
                validityGrid[r][c] = 1
            }
        }
    }

    val summedAreaTable = Array(compressedGridRows + 1) { LongArray(compressedGridCols + 1) }
    for (r in 0 until compressedGridRows) {
        for (c in 0 until compressedGridCols) {
            summedAreaTable[r + 1][c + 1] = validityGrid[r][c].toLong() +
                    summedAreaTable[r][c + 1] +
                    summedAreaTable[r + 1][c] -
                    summedAreaTable[r][c]
        }
    }

    fun isRectSumValid(compR1: Int, compC1: Int, compR2: Int, compC2: Int): Boolean {
        val sum = summedAreaTable[compR2][compC2] -
                summedAreaTable[compR1][compC2] -
                summedAreaTable[compR2][compC1] +
                summedAreaTable[compR1][compC1]
        return sum == 0L
    }

    var largestArea = 0L
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val p1 = points[i]
            val p2 = points[j]

            // Translate world coordinates to compressed grid coordinates
            val compR1 = rowMap.getValue(minOf(p1.row, p2.row))
            val compC1 = colMap.getValue(minOf(p1.col, p2.col))
            val compR2 = rowMap.getValue(maxOf(p1.row, p2.row))
            val compC2 = colMap.getValue(maxOf(p1.col, p2.col))

            // Use the summed-area table on the compressed grid
            if (isRectSumValid(compR1, compC1, compR2, compC2)) {
                val width = abs((p2.col - p1.col).toLong()) + 1
                val height = abs((p2.row - p1.row).toLong()) + 1
                largestArea = maxOf(largestArea, width * height)
            }
        }
    }

    return largestArea
}