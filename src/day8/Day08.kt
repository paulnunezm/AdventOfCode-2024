package day8

import println
import readInputForDay
import readTestInputForDay
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.system.measureTimeMillis


fun main() {
    val testInput = readTestInputForDay(8, 1)
    val testMatrix = parseMatrix(testInput)
    testMatrix.map {
        it.joinToString(separator = "") { it.toString() }
    }.onEach { println(it) }
    "\n\n".println()
    part1(testMatrix)

    val input = readInputForDay(8)
    "\n\n".println()
    Path("src/day8/input_8.txt").readText().trim().lines().onEach(::println)
    "\n\n".println()
    measureTimeMillis {
        val inputMatrix = parseMatrix(input)
        "Part 1: ${part1(inputMatrix)}".println() // Result:
    }.also { "took $it ms".println() } // 12ms
}

data class Position(val row: Int, val col: Int)

private fun parseMatrix(input: List<String>) =
    input.map { it.toList() }

private fun Char.isAntenna() = this.isLetterOrDigit()

private fun goTroughMatrix(matrix: List<List<Char>>, block: (element: Char, row: Int, col: Int) -> Unit) {
    for (row in matrix.indices) {
        for (col in matrix[0].indices) {
            block(matrix[row][col], row, col)
        }
    }
}

private fun getAntennasMap(matrix: List<List<Char>>): MutableMap<Char, List<Position>> {
    val antennasMap = mutableMapOf<Char, List<Position>>()
    goTroughMatrix(matrix) { element, row, col ->
        if (element.isAntenna()) {
            val elementPos = Position(row, col)
            val antennaPositions = antennasMap[element] ?: emptyList()
            antennasMap[element] = antennaPositions + elementPos
        }
    }
    return antennasMap
}

private fun Position.ifIsBetweenBounds(matrixLastIndex: Int): Boolean =
    this.row in 0..matrixLastIndex && this.col in 0..matrixLastIndex

private operator fun Position.minus(b: Position): Position =
    Position(
        this.row - b.row,
        this.col - b.col,
    )

private fun Position.absolute() =
    Position(
        abs(this.row),
        abs(this.col)
    )

private fun getAntiNodesForFirstPosition(
    antennasPositions: Set<Position>,
    matrixLastIndex: Int,
    positions: List<Position>,
    antiNodes: MutableSet<Position>
) {

    // Base case
    if (positions.isEmpty()) return

//    "\n === new interation".println()

    // Pre-recursion
    val current = positions.first()
    val positionsLeft = positions.drop(1)
//    "current $current, left: $positionsLeft".println()

    for (position in positionsLeft) {
        val diff = (current - position).absolute()
        val beforeAntenna = if (current.col >= position.col) {
            Position(
                current.row - diff.row,
                current.col + diff.col
            )
        } else {
            Position(
                current.row - diff.row,
                current.col - diff.col
            )
        }
        val afterAntenna = if (current.col >= position.col) {
            Position(
                position.row + diff.row,
                position.col - diff.col
            )
        } else {
            Position(
                position.row + diff.row,
                position.col + diff.col
            )
        }

        if (beforeAntenna.ifIsBetweenBounds(matrixLastIndex)
//            && !antennasPositions.contains(beforeAntenna)
        ) {
            antiNodes.add(beforeAntenna)
        }
        if (afterAntenna.ifIsBetweenBounds(matrixLastIndex)
//            && !antennasPositions.contains(afterAntenna)
        ) {
            antiNodes.add(afterAntenna)
        }
    }

    getAntiNodesForFirstPosition(antennasPositions, matrixLastIndex, positionsLeft, antiNodes)
}

private fun getAntiNodesForAntenna(
    antennasPositions: Set<Position>,
    matrixLastIndex: Int,
    positions: List<Position>
): MutableSet<Position> {
    val antiNodes = mutableSetOf<Position>()
    getAntiNodesForFirstPosition(antennasPositions, matrixLastIndex, positions, antiNodes)
    return antiNodes
}

private fun part1(matrix: List<List<Char>>): Int {
    val antennasMap = getAntennasMap(matrix)
    val antennasPositions = antennasMap.values.flatten().toSet()
    val antiNodes = mutableListOf<Position>()

    var s = 0
    for (antenna in antennasMap) {
        antiNodes += getAntiNodesForAntenna(antennasPositions, matrix.lastIndex, antenna.value)
    }
//    antiNodes.count().println()

    val finalMatrix = matrix.map {
        it.toMutableList()
    }.toMutableList()

    for (antiNode in antiNodes) {
        finalMatrix[antiNode.row][antiNode.col] = '#'
    }
    finalMatrix.map {
        it.joinToString(separator = "") { it.toString() }
    }.onEach { println(it) }
    return antiNodes.count()
}