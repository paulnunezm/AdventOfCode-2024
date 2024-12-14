package day14

import println
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * --- Day 14: Restroom Redoubt ---
 */

private data class Quadrant(val x: IntRange, val y: IntRange)
private data class Position(val x: Int, val y: Int) {

    fun up() = Position(x, y - 1)
    fun right() = Position(x + 1, y)
    fun down() = Position(x, y + 1)
    fun left() = Position(x - 1, y)

    fun isInBounds(matrix: List<List<Any>>) =
        y in matrix.indices && x in matrix.first().indices

    override fun toString(): String {
        return "($x,$y)"
    }
}

private data class Robot(var pos: Position, val velocity: Position) {
    fun move(xLastIndex: Int, yLastIndex: Int) {
        pos = Position(
            x = moveJumpingBounds(pos.x + velocity.x, xLastIndex),
            y = moveJumpingBounds(pos.y + velocity.y, yLastIndex),
        )
    }

    private fun moveJumpingBounds(location: Int, lastIndex: Int) =
        when {
            location < 0 -> lastIndex + location + 1
            location > lastIndex -> location - lastIndex - 1
            else -> location
        }
}

private fun List<Robot>.toStringImage(xLastIndex: Int, yLastIndex: Int): MutableList<String> {
    val rows = mutableListOf<String>()
    for (y in 0..yLastIndex) {
        var columns = ""
        for (x in 0..xLastIndex) {
            val robotsInPlace = count {
                it.pos.x == x && it.pos.y == y
            }
            columns += if (robotsInPlace == 0) {
                "."
            } else "$robotsInPlace"
        }
        rows.add(columns)
    }
    return rows
}

private fun List<Robot>.print(xLastIndex: Int, yLastIndex: Int) {
    toStringImage(xLastIndex, yLastIndex).println()

}

private fun List<Robot>.getRobotsForQuadrant(quadrant: Quadrant): Int =
    count {
        it.pos.x in quadrant.x && it.pos.y in quadrant.y
    }


fun main() {
    val testInput = Path("src/day14/input_test.txt")
        .readText()

    part1() // Solution: 231852216
    part2()
}

fun part1() {
    val input = Path("src/day14/input.txt")
        .readText()

    val robotList = getRobotPosList(input)

    val xLastIndex = 100
    val yLastIndex = 102

    val xHalf = (xLastIndex / 2)
    val yHalf = (yLastIndex / 2)

    val firstQuadrant = Quadrant(IntRange(0, xHalf - 1), IntRange(0, yHalf - 1))
    val secondQuadrant = Quadrant(IntRange(xHalf + 1, xLastIndex), IntRange(0, yHalf - 1))
    val thirdQuadrant = Quadrant(IntRange(0, xHalf - 1), IntRange(yHalf + 1, yLastIndex))
    val fourthQuadrant = Quadrant(IntRange(xHalf + 1, xLastIndex), IntRange(yHalf + 1, yLastIndex))

    robotList.count().println()
    robotList.forEach { robot ->
        println()
        repeat(100) {
            robot.move(xLastIndex, yLastIndex)
        }
    }
    robotList.print(10, 6)

    listOf(
        robotList.getRobotsForQuadrant(firstQuadrant),
        robotList.getRobotsForQuadrant(secondQuadrant),
        robotList.getRobotsForQuadrant(thirdQuadrant),
        robotList.getRobotsForQuadrant(fourthQuadrant)
    ).fold(1) { acc, i ->
        acc * i
    }.println()
}

fun part2() {
    val input = Path("src/day14/input.txt")
        .readText()
    val robotList = getRobotPosList(input)

    val xLastIndex = 100
    val yLastIndex = 102

    var seconds = 0
    var found = false


    while (!found) {
        seconds++
        println("seconds > $seconds")

        robotList.forEach { robot ->
            robot.move(xLastIndex, yLastIndex)
        }

        val robotString =
            robotList.toStringImage(xLastIndex, yLastIndex)
                .map { it.toList() }

        robotString.onEach { println(it) }

        val visited = mutableSetOf<Position>()

        var n = 0
        for (y in 0..yLastIndex) {
            for (x in 0..xLastIndex) {
                val x = findNeighbors(Position(x, y), visited, robotString)
                n = maxOf(n, x)
            }
        }


        if (n > 20) {
            found = true
            robotList.print(xLastIndex, yLastIndex)
        }

    }
    println("seconds $seconds")
//    robotList.print(xLastIndex, yLastIndex)
}

private fun findNeighbors(current: Position, visited: MutableSet<Position>, map: List<List<Char>>): Int {
    if (!current.isInBounds(map)) return 0
    if (current in visited) return 0

    visited += current

    if (map[current.y][current.x] == '.') return 0

    var neighbors = 1

    listOf(current.up(), current.right(), current.down(), current.left()).forEach {
        neighbors += findNeighbors(it, visited, map)
    }

    return neighbors
}

private fun getRobotPosList(input: String) =
    input.split("\n")
        .map {
            val line = it.split(" ")
            Robot(pos = inputLineToPosition(line.first()), velocity = inputLineToPosition(line.last()))
        }

private fun inputLineToPosition(s: String): Position =
    s.substringAfter("=").split(",")
        .let { Position(x = it.first().toInt(), y = it.last().toInt()) }
