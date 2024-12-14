package day13

import println
import kotlin.io.path.Path
import kotlin.io.path.readText

private data class Machine(val a: Position, val b: Position, val prize: Position)
private data class Position(val x: Long, val y: Long)

fun main() {
    val testInput = parseFileToMachines("input_test")
    val testInput2 = parseFileToMachines("input_test_2")
    val input = parseFileToMachines("input")

    part1(testInput).println() // Solution: 29522 tokens
    part2(input).println() // Solution: 101214869433312 tokens
}

private fun part1(machines: List<Machine>): Long =
    machines.sumOf {
        it.findWinningCombination().let {
            if (it.first == -1L) 0
            else it.first * 3 + it.second
        }
    }

private fun part2(machines: List<Machine>): Long =
    machines.sumOf {
        val newPrizeOffset = 10000000000000
        Machine(
            a = it.a, b = it.b,
            prize = Position(newPrizeOffset + it.prize.x, newPrizeOffset + it.prize.y)
        ).findWinningCombination2()
    }

private fun Machine.findWinningCombination2(): Long {
    val aPresses = (b.x * prize.y - b.y * prize.x) / (a.y * b.x - a.x * b.y)
    val bPresses = (prize.y - (a.y * aPresses)) / b.y

    return if (aPresses * a.x + bPresses * b.x == prize.x &&
        aPresses * a.y + bPresses * b.y == prize.y
    ) {
        println("combination found aPresses $aPresses bPresses $bPresses")
        aPresses * 3 + bPresses
    } else 0L
}

private fun Machine.findWinningCombination(): Pair<Long, Long> {
    for (aPresses in 0..100) {
        for (bPresses in 0..100) {
            val aValue = aPresses * a.x + bPresses * b.x == prize.x
            val bValue = aPresses * a.y + bPresses * b.y == prize.y
            if (aValue && bValue) {
                println("combination found aPresses $aPresses bPresses $bPresses")
                return aPresses.toLong() to bPresses.toLong()
            }
        }
    }
    return -1L to -1L
}

private fun parseFileToMachines(fileName: String): List<Machine> =
    Path("src/day13/$fileName.txt").readText()
        .split("\n\n")
        .map {
            val positions = it.split("\n").map {
                val buttonOffset = it.substringAfter(":").split(",")
                Position(
                    x = buttonOffset.first().drop(2).replace("=", "").toLong(),
                    y = buttonOffset.last().drop(2).replace("=", "").toLong()
                )
            }
            Machine(a = positions.first(), b = positions[1], prize = positions.last())
        }

