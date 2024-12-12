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

    // Solution: 1433460
    // Took: 782ms
    measureAndPrint {
        part1(getPlotMap(input))
    }
}

private fun measureAndPrint(block: (Unit) -> Int) {
    measureTime {
        """
            
        ========================
         Solution: ${block(Unit)}
        ========================
        """.trimIndent().println()
    }.also(::println) // 810.ms
}

private fun part1(map: List<List<Char>>): Int {
    val fences = mutableListOf<Pair<List<Position>, Int>>()

    val visited = mutableListOf<Position>()
    for (row in map.indices) {
        for (col in map.indices) {
            val currentPos = Position(x = col, y = row)
            if (!visited.contains(currentPos)) {
                fences += getFences(map, map[row][col], currentPos, visited)
            }
        }
    }

    return fences.sumOf {
        it.first.count() * it.second
    }
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

    println("== looking at flower $fenceType in pos $currentPos")

    fence.add(currentPos)
    visited.add(currentPos)

    // check sides
    listOf(
        currentPos.up(),
        currentPos.down(),
        currentPos.left(),
        currentPos.right()
    ).onEach {
        if (!visited.contains(it) || map[it.y][it.x] != fenceType) {
            val (f, p) = getFences(map, fenceType, it, visited)
            fence.addAll(f)
            perimeter += p
        }
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