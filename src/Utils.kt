import java.math.BigInteger
import java.security.MessageDigest
import kotlin.collections.forEachIndexed
import kotlin.io.path.Path
import kotlin.io.path.readText

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