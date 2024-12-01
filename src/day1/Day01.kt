package day1

import println
import readInputForDay
import readTestInputForDay
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (left, right) = inputToOrderedLists(input)

        var acc = 0
        for (i in 0..left.lastIndex) {
            acc += abs(left[i] - right[i])
        }

        return acc
    }

    fun part2(input: List<String>): Int {
        val (left, right) = inputToOrderedLists(input)
        "oredered Left $left".println()
        "oredered Right $right".println()

        // Get number of repetitions
        val similarityMap = right.groupBy { it }

        return left.fold(0) { acc, i ->
            val repetitions = similarityMap[i]?.size ?: 0
            val similarityValue = i * repetitions
            acc + similarityValue
        }

    }


    val testInput = readTestInputForDay(1)
    check(part1(testInput) == 11)

    val input = readInputForDay(1)
    "solution part 1: ${part1(input)}".println()

    "\n solution part 2:".println()
    part2(input).println()
}

private fun inputToOrderedLists(input: List<String>): Pair<List<Int>, List<Int>> {
    val left = ArrayList<Int>()
    val right = ArrayList<Int>()

    input.forEach {
        val i = it.split(" ")
        left.add(i.first().toInt())
        right.add(i.last().toInt())
    }

    left.sort()
    right.sort()

    return left to right
}
