package day8

import println
import readInputForDay
import readTestInputForDay
import kotlin.math.abs

fun main() {
    val testInput = readTestInputForDay(8, 1)
    val testInput2 = readTestInputForDay(8, 2)
    val testMatrix = parseMatrix(testInput)
    val testMatrix2 = parseMatrix(testInput2)
    drawMatrix(testMatrix, emptyList())
    part1(testMatrix).println()
    part2(testMatrix).println()

    val input = readInputForDay(8)
    val inputMatrix = parseMatrix(input)
    "Part 1: ${part1(inputMatrix)}".println() // Result: 396
    "Part 2: ${part2(inputMatrix)}".println() // Result: 1200
}

private data class Position(val row: Int, val col: Int)

private fun parseMatrix(input: List<String>) = input.map { it.toList() }

private fun getAntennasMap(matrix: List<List<Char>>): MutableMap<Char, List<Position>> {
    val antennasMap = mutableMapOf<Char, List<Position>>()
    for (row in matrix.indices) {
        for (col in matrix[0].indices) {
            val element = matrix[row][col]
            if (element.isLetterOrDigit()) { // is antenna
                val elementPos = Position(row, col)
                val antennaPositions = antennasMap[element] ?: emptyList()
                antennasMap[element] = antennaPositions + elementPos
            }
        }
    }
    return antennasMap
}

private fun Position.ifIsBetweenBounds(indices: IntRange): Boolean =
    this.row in indices && this.col in indices

private operator fun Position.minus(b: Position): Position =
    Position(this.row - b.row, this.col - b.col)

private fun Position.absolute() = Position(abs(this.row), abs(this.col))

private fun getAntiNodesForFirstPosition(
    frequenciesPositions: Set<Position>,
    positions: List<Position>,
    antiNodes: MutableSet<Position>,
    calculateHarmonics: Boolean = false,
    matrixIndices: IntRange
) {

    // Base case
    if (positions.size == 1 || positions.isEmpty()) return

    // Pre-recursion
    val current = positions.first()
    val positionsLeft = positions.drop(1)

    for (position in positionsLeft) {
        val diff = (current - position).absolute()

        val isCurrentToTheRight = current.col >= position.col
        val beforeAntiNodeColPos = if (isCurrentToTheRight) {
            current.col + diff.col
        } else current.col - diff.col

        var beforeAntiNode = Position(
            current.row - diff.row,
            beforeAntiNodeColPos
        )
        val afterAntiNodeColPos = if (isCurrentToTheRight) {
            position.col - diff.col
        } else position.col + diff.col

        var afterAntiNode = Position(
            position.row + diff.row,
            afterAntiNodeColPos,
        )
        if (!calculateHarmonics) {
            antiNodes.add(beforeAntiNode)
            antiNodes.add(afterAntiNode)
        } else {
            while (beforeAntiNode.col in matrixIndices && beforeAntiNode.row in matrixIndices) {
                antiNodes.add(beforeAntiNode)
                beforeAntiNode = Position(
                    beforeAntiNode.row - diff.row,
                    if (isCurrentToTheRight) {
                        beforeAntiNode.col + diff.col
                    } else beforeAntiNode.col - diff.col
                )
            }
            while (afterAntiNode.col in matrixIndices && afterAntiNode.row in matrixIndices) {
                antiNodes.add(afterAntiNode)

                afterAntiNode = Position(
                    afterAntiNode.row + diff.row,
                    if (isCurrentToTheRight) {
                        afterAntiNode.col - diff.col
                    } else afterAntiNode.col + diff.col
                )
            }
        }
    }

    getAntiNodesForFirstPosition(frequenciesPositions, positionsLeft, antiNodes, calculateHarmonics, matrixIndices)
}

private fun getAntiNodesForAntenna(
    frequenciesPositions: Set<Position>,
    positions: List<Position>,
    calculateHarmonics: Boolean,
    matrixIndices: IntRange
): MutableSet<Position> {
    val antiNodes = mutableSetOf<Position>()
    getAntiNodesForFirstPosition(frequenciesPositions, positions, antiNodes, calculateHarmonics, matrixIndices)
    return antiNodes
}

private fun drawMatrix(matrix: List<List<Char>>, antiNodes: List<Position>) {
    val finalMatrix = matrix.map {
        it.toMutableList()
    }.toMutableList()

    for (antiNode in antiNodes) {
        finalMatrix[antiNode.row][antiNode.col] = '#'
    }
    finalMatrix.map {
        it.joinToString(separator = "") { it.toString() }
    }.onEach { println(it) }
}

private fun part1(matrix: List<List<Char>>): Int {
    val antennasMap = getAntennasMap(matrix)
    val frequenciesPositions = antennasMap.values.flatten().toSet()
    val antiNodes = antennasMap.map {
        getAntiNodesForAntenna(frequenciesPositions, it.value, calculateHarmonics = false, matrix.indices)
    }.flatten().filter {
        it.ifIsBetweenBounds(matrix.indices)
    }.distinct()

    drawMatrix(matrix, antiNodes)

    return antiNodes.count()
}

private fun part2(matrix: List<List<Char>>): Int {
    val antennasMap = getAntennasMap(matrix)
    val frequenciesPositions = antennasMap.values.flatten().toSet()
    frequenciesPositions.println()
    val antiNodes = antennasMap.map {
        getAntiNodesForAntenna(frequenciesPositions, it.value, calculateHarmonics = true, matrix.indices)
    }.flatten().filter {
        it.ifIsBetweenBounds(matrix.indices) && !frequenciesPositions.contains(it) // Because we sum it later
    }.distinct()

    drawMatrix(matrix, antiNodes)

    return antiNodes.count() + frequenciesPositions.count()
}
