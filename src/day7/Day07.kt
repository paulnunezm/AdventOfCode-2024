package day7

import println
import readInput
import readInputForDay
import readTestInputForDay
import kotlin.system.measureTimeMillis

private data class Equation(val result: Long, val values: List<Long>)

fun main() {
    val testInput = transformInput(readTestInputForDay(7, part = 1))
    val input = transformInput(readInputForDay(7))

    part1(testInput).also {
        check(it == 3749L) { "test result was $it" }
    }

    measureTimeMillis {
        "Part 1: ${part1(input)}".println() // Result: 6242
    }.also { "took $it ms".println() } // 9ms
}

private fun part1(input: List<Equation>): Long =
    input.sumOf { equation ->
        val permutations = operationPermutation(equation)
        if (equation.result in permutations) {
            equation.result
        } else {
            0
        }
    }

private fun operationPermutation(equation: Equation): List<Long> {
    val permutations = equation.values.fold(emptyList()) { acc: List<Long>, i: Long ->
        if (acc.isEmpty()) {
            return@fold listOf(i)
        } else {
            val x = mutableListOf<Long>()
            for (j in acc.indices) {
                x += acc[j] + i
                x += acc[j] * i
            }
            return@fold x
        }
    }
    return permutations
}

private fun transformInput(input: List<String>) =
    input.flatMap { it.split(",") }
        .map {
            val result = it.substringBefore(":").toLong()
            val values = it.substringAfter(":").trim().split(" ").map { it.toLong() }
            Equation(result, values)
        }
