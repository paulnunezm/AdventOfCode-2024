package day2

import println
import readInputForDay
import readTestInputForDay
import kotlin.math.abs
import kotlin.system.measureTimeMillis

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


    fun List<Long>.isSafeReport2(): Boolean {
        // Safety systems
        var areAllIncreasing = true
        var areAllDecreasing = true

        this.windowed(2).forEach {
            val left = it.first()
            val right = it.last()
            val levelDiff = (right - left).toInt()
            val levelDiffAbs = abs(levelDiff)

            if (levelDiff == 0 || levelDiffAbs > 3) return false

            if (levelDiff > 0) {
                areAllDecreasing = false
            } else {
                areAllIncreasing = false
            }
            if (!areAllIncreasing.xor(areAllDecreasing)) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>, safeReportFun: List<Long>.() -> Boolean): Int =
        input.toReports()
            .map {
                it.safeReportFun()
            }
            .count { it }

    fun part2(input: List<String>, safeReportFun: List<Long>.() -> Boolean): Int =
        input.toReports()
            .map { report ->
                if (report.safeReportFun()) {
                    true
                } else {
                    List(report.size) { index ->
                        report.toMutableList()
                            .apply { removeAt(index) }
                            .safeReportFun()
                    }.any { it }
                }
            }.count { it }


    val testInput = readTestInputForDay(2)
    part1(testInput, List<Long>::isSafeReport).also {
        check(it == 2) { "test result was $it" }
    }
    part2(testInput, List<Long>::isSafeReport).also {
        check(it == 4) { "2nd test result was $it" }
    }

    val input = readInputForDay(2)

    measureTimeMillis {
        "Part 1: ${part1(input, List<Long>::isSafeReport)}".println() // Result: 624
    }.also { "took $it ms".println() } // 6ms

    measureTimeMillis {
        "Part 1 v2: ${part1(input, List<Long>::isSafeReport2)}".println()
    }.also { "took $it ms".println() } // 3ms, 50% improve.

    measureTimeMillis {
        "\nPart 2: ${part2(input, List<Long>::isSafeReport)}".println() // Result: 658
    }.also { "Part 2 v1 took $it ms".println() } // 3ms

    measureTimeMillis {
        "Part 2 v2: ${part2(input, List<Long>::isSafeReport2)}".println()
    }.also { "took $it ms".println() } // 2ms. The second iteration is the bottleneck.
}