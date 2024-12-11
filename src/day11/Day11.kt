package day11

import println
import readInputForDay
import readTestInputForDay
import kotlin.system.measureTimeMillis

/**
 * == Plutonian Pebbles ==
 */

sealed interface Rule {
    fun check(pebbleIdx: Int, pebbles: MutableList<Long>): Boolean

    /**
     * @return last index of added pebbles
     */
    fun apply(pebbleIdx: Int, pebbles: MutableList<Long>): Int
}

object ZeroRule : Rule {
    override fun check(pebbleIdx: Int, pebbles: MutableList<Long>) =
        pebbles[pebbleIdx] == 0L

    override fun apply(pebbleIdx: Int, pebbles: MutableList<Long>): Int {
        pebbles[pebbleIdx] = 1
        return pebbleIdx
    }
}

object EvenDigitsRule : Rule {
    override fun check(pebbleIdx: Int, pebbles: MutableList<Long>): Boolean {
        val pebbleEngrave = pebbles[pebbleIdx]
        return pebbleEngrave.toString().count() % 2 == 0
    }

    override fun apply(pebbleIdx: Int, pebbles: MutableList<Long>): Int {
        val engrave = pebbles[pebbleIdx].toString()
        var left = 0L
        var right = 0L
        if (engrave.count() == 2) {
            left = engrave[0].digitToInt().toLong()
            right = engrave[1].digitToInt().toLong()
        } else {
            val midIdx = engrave.count() / 2
            left = engrave.slice(0..<midIdx).toLong()
            right = engrave.slice(midIdx..engrave.lastIndex).toLong()
        }
        pebbles[pebbleIdx] = left
        pebbles.add(pebbleIdx + 1, right)
        return pebbleIdx + 1
    }
}

object ReplaceRule : Rule {
    override fun check(pebbleIdx: Int, pebbles: MutableList<Long>) = true
    override fun apply(pebbleIdx: Int, pebbles: MutableList<Long>): Int {
        val pebbleEngrave = pebbles[pebbleIdx]
        pebbles[pebbleIdx] = pebbleEngrave * 2024L
        return pebbleIdx
    }
}

private fun List<String>.toPebbleList() =
    this.first()
        .split(" ")
        .map { it.toLong() }

private fun splitPebbles(pebbles: MutableList<Long>) {
    var pebbleIdx = 0
    while (pebbleIdx in pebbles.indices) {
        val lastAddedPebbleIndex = when {
            ZeroRule.check(pebbleIdx, pebbles) -> ZeroRule.apply(pebbleIdx, pebbles)
            EvenDigitsRule.check(pebbleIdx, pebbles) -> EvenDigitsRule.apply(pebbleIdx, pebbles)
            else -> ReplaceRule.apply(pebbleIdx, pebbles)
        }
        pebbleIdx = lastAddedPebbleIndex + 1
    }
}

private fun blink(times: Int, pebbles: List<Long>): Int {
    val mutablePebbles = pebbles.toMutableList()
    repeat(times) {
        splitPebbles(mutablePebbles)
    }
    return mutablePebbles.count()
}

fun main() {
    val testPebbles = readTestInputForDay(11, part = 1).toPebbleList()
    val pebbles = readInputForDay(11).toPebbleList()
    part1(testPebbles).println()

    measureTimeMillis {
        "Part 1: ${part1(pebbles)}".println() // Result: 187738
    }.also { "took $it ms".println() } // 617ms

//    measureTimeMillis {
//        "Part 2: ${part2(pebbles)}".println() // Result: 187738
//    }.also { "took $it ms".println() } // 617ms
}
private fun part1(pebbles: List<Long>): Int = blink(25, pebbles)

private fun part2(pebbles: List<Long>): Int = blink(75, pebbles)
