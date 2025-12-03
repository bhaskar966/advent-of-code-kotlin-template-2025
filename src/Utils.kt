import java.lang.Math.pow
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.pow

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