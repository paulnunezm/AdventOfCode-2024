package day3

import println
import readInputForDay
import readTestInputForDay
import kotlin.system.measureTimeMillis

fun main() {

    fun part1(testInput: List<String>): Long {
        val regex = "mul\\(\\d{0,3},\\d{0,3}\\)".toRegex()
        val matches = mutableListOf<String>()

        testInput.forEach {
            regex.findAll(it).forEach { result ->
                matches.add(result.value)
            }
        }

        return matches.map {
            it.substringAfter("(")
                .substringBefore(")")
                .split(",")
                .map { it.toLong() }
        }.sumOf {
            it.first() * it.last()
        }
    }

    fun part2(testInput: List<String>): Long {
        val regex =
            "do\\(\\).*?mul\\(\\d{0,3},\\d{0,3}\\)|mul\\(\\d{0,3},\\d{0,3}\\)|don't\\(\\).*?mul\\(\\d{0,3},\\d{0,3}\\)".toRegex()
        val matches = emptyList<String>().toMutableList()

        testInput.forEach {
            regex.findAll(it).forEach { result ->
                matches.add(result.value)
            }
        }

        var openDont = false
        val idxToRemove = mutableListOf<Int>()
        matches.println()
        matches.forEachIndexed { index, s ->
            if (s.contains("don't()")) {
                openDont = true
            }
            if (s.contains("do()")) {
                openDont = false
            }
            if (openDont) idxToRemove.add(index)
        }

        idxToRemove.forEach { matches[it] = "" }

        return matches
            .filterNot { it.isEmpty() }
            .map {
                it.substringAfter("mul(")
                    .substringBefore(")")
                    .also(::println)
                    .split(",")
                    .map { it.toLong() }
            }.sumOf {
                it.first() * it.last()
            }
    }

    val input = readInputForDay(3)

    part1(readTestInputForDay(3)).also {
        check(it == 161L) { "test result was $it" }
    }
    part2(readTestInputForDay(3, part = 2)).also {
        check(it == 48L) { "2nd test result was $it expecting 48" }
    }

    measureTimeMillis {
        "Part 1: ${part1(input)}".println() // Result: 178886550
    }.also { "took $it ms".println() } // 15ms

    measureTimeMillis {
        "\nPart 2: ${part2(input)}".println() // Result: 87163705
    }.also { "Part 2 took $it ms".println() } // 6ms
}