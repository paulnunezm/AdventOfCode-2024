package day12

import println
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.time.measureTime

fun main() {
    val testInput1 =
        Path("src/day12/input_test_1.txt").readText().trim().lines()
    val input =
        Path("src/day12/input_12.txt").readText().trim().lines()

    val testGardensPlots = getPlotMap(testInput1)
    measureTime {
        part1(getPlotMap(input))// Solution: 1433460
    }.println() // 949ms
}

private fun part1(map: List<List<Char>>) {
    val fences = mutableListOf<Pair<List<Position>, Int>>()

    val visited = mutableListOf<Position>()
    for (row in map.indices) {
        for (col in map.indices) {
            val currentPos = Position(x = col, y = row)
            if (!visited.contains(currentPos)) {
                fences += getFences(map, map[row][col], currentPos, visited)
                println("visited for ${map[row][col]}: \n$visited}")
            }
        }
    }

    fences.sumOf {
        it.first.count() * it.second
    }.println()
}

private fun getFences(
    map: List<List<Char>>,
    fenceType: Char,
    currentPos: Position,
    visited: MutableList<Position>
): Pair<MutableList<Position>, Int> {
    var fence = mutableListOf<Position>()
    var perimeter = 0

    // Base case
    if (!currentPos.isInBounds(map)) {
        return fence to 1
    }

    if (map[currentPos.y][currentPos.x] != fenceType) {
        return fence to 1
    }

    println("==checking $fenceType")

    fence.add(currentPos)
    visited.add(currentPos)

    // check sides
    if (!visited.contains(currentPos.up()) || map[currentPos.up().y][currentPos.up().x] != fenceType ) {
        val (f, p) = getFences(map, fenceType, currentPos.up(), visited)
        fence.addAll(f)
        perimeter += p
    }
    if (!visited.contains(currentPos.down()) || map[currentPos.down().y][currentPos.down().x] != fenceType ) {
        val (f, p) = getFences(map, fenceType, currentPos.down(), visited)
        fence.addAll(f)
        perimeter += p
    }
    if (!visited.contains(currentPos.left()) || map[currentPos.left().y][currentPos.left().x] != fenceType ) {
        val (f, p) = getFences(map, fenceType, currentPos.left(), visited)
        fence.addAll(f)
        perimeter += p
    }
    if (!visited.contains(currentPos.right()) || map[currentPos.right().y][currentPos.right().x] != fenceType ) {
        val (f, p) = getFences(map, fenceType, currentPos.right(), visited)
        fence.addAll(f)
        perimeter += p
    }

    return fence to perimeter
}

private fun getPlotMap(input: List<String>) =
    input.mapIndexed { y, s ->
        buildList {
            s.toList().forEachIndexed { x, c ->
                add(c)
            }
        }
    }

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

private data class GardenPlot(val flowerType: Char, val pos: Position)