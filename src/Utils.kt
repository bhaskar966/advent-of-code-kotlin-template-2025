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