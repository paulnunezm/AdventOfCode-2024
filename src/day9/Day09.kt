package day9

import println
import readInputForDay
import readTestInputForDay
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {
    val testDiskMap = readTestInputForDay(9, part = 1).first()
    val diskMap = readInputForDay(9).first()

    measureTimeMillis {
        "Part 1: ${part1(diskMap)}".println() // Result: 6334655979668
    }.also { "took $it ms".println() } // 13ms
}

private fun convertDiskMap(diskMap: String): MutableList<String> {
    val individualBlocks = mutableListOf<String>()
    var lastBlockFileId = -1
    diskMap.forEachIndexed { index: Int, c: Char ->
        var blockToAdd = "."
        if (index % 2 == 0) {
            lastBlockFileId++
            blockToAdd = lastBlockFileId.toString()
        }
        c.digitToIntOrNull()?.let {
            for (i in 1..it) {
                individualBlocks += blockToAdd
            }
        }
    }
    return individualBlocks
}

private fun part1(diskMap: String): Long {
    val individualBlocks = convertDiskMap(diskMap)

    var l = individualBlocks.indexOfFirst { it == "." }
    var r = individualBlocks.indexOfLast { it != "." }
    while (l < r) {
        when {
            individualBlocks[l] != "." -> {
                l++
                continue
            }

            individualBlocks[r] == "." -> {
                r--
                continue
            }
        }
        individualBlocks[l] = individualBlocks[r]
        individualBlocks[r] = "."
    }

    var sum = 0L
    for (i in individualBlocks.indices) {
        if (individualBlocks[i] == ".")
            break
        sum += i * individualBlocks[i].toLong()
    }
    return sum
}