import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(path: String, name: String) =
    Path("$path/$name.txt").readText().trim().lines()

fun readInputForDay(day: Int) =
    readInput("src/day$day","input_$day")

fun readTestInputForDay(day: Int) =
    readInput("src/day$day","input_test_$day")
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
