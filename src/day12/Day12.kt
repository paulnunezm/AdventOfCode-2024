package day12

import println
import readTestInputForDay
import javax.sql.ConnectionPoolDataSource

private typealias Fence = List<GardenPlot>

fun main() {
    val testInput1 = readTestInputForDay(12, 1)
    val testGardensPlots = getPlotMap(testInput1)
    part1(testGardensPlots)
}

private fun part1(map: List<List<Char>>) {
    val visited = mutableListOf<Position>()
    val (f, p) = getFences(map, 'D', Position(3, 1), visited)
    println("f:$f \np:$p")
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
        perimeter++
        return fence to perimeter
    }

    if (map[currentPos.y][currentPos.x] != fenceType) {
        perimeter++
        return fence to perimeter
    }

    visited.add(currentPos)
    fence.add(currentPos)

    // check sides
    if (!visited.contains(currentPos.up())) {
        "visiting up".println()
        val (f, p) = getFences(map, fenceType, currentPos.up(), visited)
        fence.addAll(f)
        perimeter += p
    }
    if (!visited.contains(currentPos.down())) {
        val (f, p) = getFences(map, fenceType, currentPos.down(), visited)
        "visiting up".println()
        fence.addAll(f)
        perimeter += p
    }
    if (!visited.contains(currentPos.left())) {
        val (f, p) = getFences(map, fenceType, currentPos.left(), visited)
        "visiting up".println()
        fence.addAll(f)
        perimeter += p
    }
    if (!visited.contains(currentPos.right())) {
        "visiting up".println()
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