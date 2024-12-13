package day13

import println
import kotlin.io.path.Path
import kotlin.io.path.readText

private data class Machine(val buttonAOffset: Position, val buttonBOffset: Position, val prize: Position)
private data class Position(val x: Int, val y: Int)

fun main() {
    val testInput = parseFileToMachines("input_test")
    val testInput2 = parseFileToMachines("input_test_2")
    val input = parseFileToMachines("input")

    part1(input).println() // Solution: 29522 tokens
}

private fun part1(machines: List<Machine>): Int =
    machines.sumOf {
        it.findWinningCombination().let {
            if (it.first == -1) 0
            else it.first * 3 + it.second
        }
    }

private fun part2(machines: List<Machine>): Int =
    machines.sumOf {
        it.findWinningCombination().let {
            if (it.first == -1) 0
            else it.first * 3 + it.second
        }
    }

private fun Machine.findWinningCombination(): Pair<Int, Int> {
    for (aPresses in 0..100) {
        for (bPresses in 0..100) {
            val a = aPresses * buttonAOffset.x + bPresses * buttonBOffset.x == prize.x
            val b = aPresses * buttonAOffset.y + bPresses * buttonBOffset.y == prize.y
            if (a && b) {
                println("combination found aPresses $aPresses bPresses $bPresses")
                return aPresses to bPresses
            }
        }
    }
    return -1 to -1
}

private fun parseFileToMachines(fileName: String): List<Machine> =
    Path("src/day13/$fileName.txt").readText()
        .split("\n\n")
        .map {
            val positions = it.split("\n").map {
                val buttonOffset = it.substringAfter(":").split(",")
                Position(
                    x = buttonOffset.first().drop(2).replace("=", "").toInt(),
                    y = buttonOffset.last().drop(2).replace("=", "").toInt()
                )
            }
            Machine(buttonAOffset = positions.first(), buttonBOffset = positions[1], prize = positions.last())
        }

