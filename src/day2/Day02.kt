package day2

import println
import readInputForDay
import readTestInputForDay
import kotlin.math.abs

fun main() {

    fun List<String>.toReports() =
        this.asSequence()
            .map { line ->
                line.split(" ")
            }.map { reportsStrings ->
                reportsStrings.map { it.toLong() }
            }

    fun List<Long>.isSafeReport(): Boolean {
        // Safety systems
        var areAllIncreasing = true
        var areAllDecreasing = true
        var isLevelSafe = true

        this.windowed(2).forEach {
            val left = it.first()
            val right = it.last()
            val levelDiff = (right - left).toInt()
            val levelDiffAbs = abs(levelDiff)

            if (levelDiff == 0 || levelDiffAbs > 3) isLevelSafe = false
            if (levelDiff > 0) {
                areAllDecreasing = false
            } else {
                areAllIncreasing = false
            }
        }
        return isLevelSafe && areAllIncreasing.xor(areAllDecreasing)
    }

    fun part1(input: List<String>): Int =
        input.toReports()
            .map { it.isSafeReport() }
            .count { it }

    fun part2(input: List<String>): Int =
        input.toReports()
            .map { report ->
                if (report.isSafeReport()) {
                    true
                } else {
                    List(report.size) { index ->
                        val r = report.toMutableList()
                        r.removeAt(index)
                        r.isSafeReport()
                    }.any { it }
                }
            }.count { it }

    val testInput = readTestInputForDay(2)
    part1(testInput).also {
        check(it == 2) { "test result was $it" }
    }
    part2(testInput).also {
        check(it == 4) { "2nd test result was $it" }
    }

    val input = readInputForDay(2)
    part1(input).println() // Result: 624
    part2(input).println() // Result: 658

}