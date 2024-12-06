package day6

import println
import readInputForDay
import readTestInputForDay
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {

    data class Position(val x: Int, val y: Int)
    data class Direction(val x: Int, val y: Int)

    val directions = listOf(
        Direction(0, -1), // top
        Direction(1, 0), // right
        Direction(0, 1), // bottom
        Direction(-1, 0), // left
    )

    fun String.direction(): Direction {
        return when (this) {
            "^" -> directions[0]
            ">" -> directions[1]
            "v" -> directions[2]
            else -> directions[3]
        }
    }

    fun isGuardChar(s: String): Boolean {
        return when (s) {
            "^" -> true
            ">" -> true
            "v" -> true
            "<" -> true
            else -> false
        }
    }

    fun rotateDirection(direction: Direction): Direction {
        val idx = directions.indexOf(direction)
        return if (idx + 1 > directions.lastIndex) {
            directions[0]
        } else {
            directions[idx + 1]
        }
    }

    fun walk(
        matrix: List<List<String>>,
        currentPosition: Position,
        direction: Direction,
        visited: MutableList<Position>
    ) {
        var nextPosition = Position(currentPosition.x + direction.x, currentPosition.y + direction.y)

        //  == base cases ==
        val element = try { // Out of bounds
            matrix[nextPosition.y][nextPosition.x]
        } catch (e: IndexOutOfBoundsException) {
            visited.add(currentPosition)
            return
        }

        var nextDirection = direction
        if (element == "#") { // We hit a wall
            println("wall hit")
            nextPosition = currentPosition
            nextDirection = rotateDirection(direction)
            println("new dir $nextDirection")
        } else {
            visited.add(currentPosition)
        }

        // recursion
        walk(matrix, nextPosition, nextDirection, visited)
    }

    fun findGuardPosition(matrix: List<List<String>>): Position {
        for (y in matrix.indices) {
            for (x in matrix[y].indices) {
                if (isGuardChar(matrix[y][x])) {
                    return Position(x = x, y = y)
                }
            }
        }
        return Position(x = -1, y = -1)
    }

    fun part1(input: List<String>): Int {
        val matrix = input.map {
            it.split(" ")
                .flatMap {
                    it.split("")
                        .filterNot { it.isEmpty() }
                }
        }.toMutableList()

        val visitedPositions = mutableListOf<Position>()
        val initialPos = findGuardPosition(matrix)
        val initialDir = matrix[initialPos.y][initialPos.x].direction()
        "initial $initialPos initial dir $initialDir".println()

        visitedPositions.add(initialPos)
        walk(matrix, initialPos, initialDir, visitedPositions)

        for (position in visitedPositions) {
            val x = matrix[position.y].toMutableList()
            x[position.x] = "x"
            matrix[position.y] = x
        }

        matrix.onEach(::println)
        return visitedPositions.distinct().count()
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    part1(readTestInputForDay(6, part = 1)).also {
        check(it == 41) { "test result was $it" }
    }
//    part2(readTestInputForDay(5, part = 1)).also {
//        check(it == 123) { "test result was $it" }
//    }

    val input = readInputForDay(6)
    measureTimeMillis {
        "Part 1: ${part1(input)}".println() // Result: 6242
    }.also { "took $it ms".println() } // 9ms
//
//    measureTimeMillis {
//        "Part 2: ${part2(input)}".println() // Result: 5169
//    }.also { "took $it ms".println() } // 29ms
//
}