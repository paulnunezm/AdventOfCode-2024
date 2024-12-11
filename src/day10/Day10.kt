package day10

import println
import readInputForDay
import readTestInputForDay
import kotlin.system.measureTimeMillis

private data class Path(val height: Int, val position: Position)
private data class Position(val row: Int, val col: Int)
private data class Direction(val col: Int, val row: Int)

private val directions = listOf(
    Direction(0, -1), // top
    Direction(1, 0), // right
    Direction(0, 1), // bottom
    Direction(-1, 0), // left
)

fun main() {
    val testInput = readTestInputForDay(10, part = 1)
        .map {
            it.map { it.digitToInt() }
        }
    val input = readInputForDay(10)
        .map {
            it.map { it.digitToInt() }
        }

    part1(testInput).println()

    measureTimeMillis {
        "Part 1: ${part1(input)}".println() // Result: 682
    }.also { "took $it ms".println() } // 4ms

    measureTimeMillis {
        "Part 2: ${part2(input)}".println() // Result: 1511
    }.also { "took $it ms".println() } // 1ms
}

private fun Position.ifIsBetweenBounds(indices: IntRange): Boolean =
    this.row in indices && this.col in indices

private fun walkTroughPaths(currentPath: Path, map: List<List<Int>>): Set<Position> {
    val trailEndPosition = mutableSetOf<Position>()
    for (idx in directions.indices) {
        val direction = directions[idx]
        val nextPathPosition = Position(
            row = currentPath.position.row + direction.row,
            col = currentPath.position.col + direction.col,
        )
        if (nextPathPosition.ifIsBetweenBounds(map.indices)) {
            val nextHeight = map[nextPathPosition.row][nextPathPosition.col]
            if (nextHeight == currentPath.height + 1) {
                if (nextHeight == 9) {
                    trailEndPosition.add(nextPathPosition)
                } else {
                    val newEndPos = walkTroughPaths(
                        currentPath = Path(height = nextHeight, nextPathPosition),
                        map = map
                    )
                    trailEndPosition.addAll(newEndPos)
                }
            }

        }
    }

    return trailEndPosition
}

private fun walkTroughPaths2(currentPath: Path, map: List<List<Int>>): Int {
    var trails = 0
    for (idx in directions.indices) {
        val direction = directions[idx]
        val nextPathPosition = Position(
            row = currentPath.position.row + direction.row,
            col = currentPath.position.col + direction.col,
        )
        if (nextPathPosition.ifIsBetweenBounds(map.indices)) {
            val nextHeight = map[nextPathPosition.row][nextPathPosition.col]
            if (nextHeight == currentPath.height + 1) {
                if (nextHeight == 9) {
                    trails++
                } else {
                    trails += walkTroughPaths2(
                        currentPath = Path(height = nextHeight, nextPathPosition),
                        map = map
                    )
                }
            }

        }
    }

    return trails
}

private fun findTrails(startPosition: Path, map: List<List<Int>>): Int =
    walkTroughPaths(startPosition, map).count()

private fun findTrails2(startPosition: Path, map: List<List<Int>>): Int =
    walkTroughPaths2(startPosition, map)

private fun part1(map: List<List<Int>>): Int {
    val trailHeads = mutableListOf<Path>()
    map.forEachIndexed { rowIdx, row ->
        row.forEachIndexed { colIdx, col ->
            if (col == 0) trailHeads += Path(0, Position(rowIdx, colIdx))
        }
    }

    return trailHeads.fold(0) { acc, pos ->
        acc + findTrails(pos, map)
    }
}

private fun part2(map: List<List<Int>>): Int {
    val trailHeads = mutableListOf<Path>()
    map.forEachIndexed { rowIdx, row ->
        row.forEachIndexed { colIdx, col ->
            if (col == 0) trailHeads += Path(0, Position(rowIdx, colIdx))
        }
    }

    return trailHeads.fold(0) { acc, pos ->
        acc + findTrails2(pos, map)
    }
}