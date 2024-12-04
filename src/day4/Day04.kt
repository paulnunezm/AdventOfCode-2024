package day4

import println
import readInputForDay
import readTestInputForDay
import kotlin.system.measureTimeMillis

fun main() {

    fun getMatrix(input: List<String>) =
        input.map {
            it.split("").filterNot { c ->
                c.isEmpty()
            }
        }

    fun checkForWordAtLine(
        word: List<String> = listOf("X", "M", "A", "S"),
        matrix: List<List<String>>, pos: (i: Int) -> Pair<Int, Int>
    ): Int {
        var wordsFound = 0
        for (i in word.indices) {
            val idxPair = pos.invoke(i)
            val rowIdx = idxPair.first
            val colIdx = idxPair.second
            val currentLetter = matrix[rowIdx][colIdx]

            "at: r:$rowIdx, c:$colIdx, i:$i, currentLetter $currentLetter".println()
            "current: $currentLetter letterToCheck: ${word[i]}".println()
            if (currentLetter != word[i]) break
            if (i == word.lastIndex) wordsFound++
        }
        return wordsFound
    }

    fun checkForWord(matrix: List<List<String>>, rowIdx: Int, colIdx: Int): Int {
        var wordCount = 0

        val xMasCount = 3 // xmas count - 1
        val canCheckBT = rowIdx - xMasCount >= 0
        val canCheckTB = rowIdx + xMasCount <= matrix.lastIndex
        val canCheckLR = colIdx + xMasCount <= matrix.lastIndex
        val canCheckRL = colIdx - xMasCount >= 0

        if (canCheckBT) {
            wordCount += checkForWordAtLine(matrix = matrix) { i ->
                rowIdx - i to colIdx
            }.also {
                "->BT $it".println()
            }

            if (canCheckLR) {
                wordCount += checkForWordAtLine(matrix = matrix) { i ->
                    rowIdx - i to colIdx + i
                }.also {
                    "->BT Diagonal LR $it".println()
                }
            }

            if (canCheckRL) {
                wordCount += checkForWordAtLine(matrix = matrix) { i ->
                    rowIdx - i to colIdx - i
                }.also {
                    "->BT Diagonal RL $it".println()
                }
            }
        }

        if (canCheckTB) {
            wordCount += checkForWordAtLine(matrix = matrix) { i -> rowIdx + i to colIdx }
                .also {
                    "->TB $it".println()
                }

            if (canCheckLR) {
                wordCount += checkForWordAtLine(matrix = matrix) { i ->
                    rowIdx + i to colIdx + i
                }.also {
                    "->TB Diagonal LR$it".println()
                }
            }

            if (canCheckRL) {
                wordCount += checkForWordAtLine(matrix = matrix) { i ->
                    rowIdx + i to colIdx - i
                }.also {
                    "->TB Diagonal RL $it".println()
                }
            }
        }

        if (canCheckLR) {
            wordCount += checkForWordAtLine(matrix = matrix) { i -> rowIdx to colIdx + i }
                .also {
                    "->LR $it".println()
                }
        }

        if (canCheckRL) {
            wordCount += checkForWordAtLine(matrix = matrix) { i -> rowIdx to colIdx - i }
                .also {
                    "->RL $it".println()
                }
        }

        return wordCount
    }

    fun hasMasInX(matrix: List<List<String>>, rowIdx: Int, colIdx: Int): Boolean {
        var wordCount = 0
        val wordList = listOf("M", "A", "S")
        val offset = 1
        val canCheckBT = rowIdx - offset >= 0
        val canCheckTB = rowIdx + offset <= matrix.lastIndex
        val canCheckLR = colIdx + offset <= matrix.lastIndex
        val canCheckRL = colIdx - offset >= 0

        if (canCheckLR && canCheckRL && canCheckTB && canCheckBT) {

            wordCount += checkForWordAtLine(word = wordList, matrix = matrix) { i ->
                rowIdx + i - offset to colIdx + i - offset
            }.also {
                "->TB Diagonal LR $it".println()
            }

            wordCount += checkForWordAtLine(word = wordList, matrix = matrix) { i ->
                rowIdx + i - offset to colIdx - i + offset
            }.also {
                "->TB Diagonal RL $it".println()
            }

            wordCount += checkForWordAtLine(word = wordList, matrix = matrix) { i ->
                rowIdx - i + offset to colIdx + i - offset
            }.also {
                "->BT Diagonal LR $it".println()
            }

            wordCount += checkForWordAtLine(word = wordList, matrix = matrix) { i ->
                rowIdx - i + offset to colIdx - i + offset
            }.also {
                "->BT Diagonal RL $it".println()
            }

        }

        return wordCount == 2
    }

    fun part1(input: List<String>): Int {
        val wordMatrix = getMatrix(input)
            .onEach(::println)

        var words = 0
        for (rowIdx in wordMatrix.indices) {
            val row = wordMatrix[rowIdx]
            for (columnIdx in row.indices) {
                if (row[columnIdx] == "X") {
                    "X at $rowIdx,$columnIdx".println()
                    words += checkForWord(wordMatrix, rowIdx, columnIdx)
                    "\n==".println()
                }
            }
        }

        "total word = $words".println()
        return words
    }


    fun part2(input: List<String>): Int {
        val wordMatrix = getMatrix(input)
            .onEach(::println)

        var words = 0
        for (rowIdx in wordMatrix.indices) {
            val row = wordMatrix[rowIdx]
            for (columnIdx in row.indices) {
                if (row[columnIdx] == "A") {
                    "A at $rowIdx,$columnIdx".println()
                    if (hasMasInX(wordMatrix, rowIdx, columnIdx)) {
                        words++
                    }
                    "\n==".println()
                }
            }
        }

        "total word = $words".println()
        return words
    }

    part1(readTestInputForDay(4, part = 1)).also {
        check(it == 18) { "test result was $it" }
    }
    part2(readTestInputForDay(4, part = 1)).also {
        check(it == 9) { "test result was $it" }
    }

    val input = readInputForDay(4)
    measureTimeMillis {
        "Part 1: ${part1(input)}".println() // Result: 2618
    }.also { "took $it ms".println() } // 65ms

    measureTimeMillis {
        "Part 2: ${part2(input)}".println() // Result: 2011
    }.also { "took $it ms".println() } // 79ms
}
