package day5

import println
import readInputForDay
import readTestInputForDay
import kotlin.system.measureTimeMillis

fun main() {

    /**
     * Key is the one that should be before
     */
    fun getRulesMap(orderingRulesInput: List<String>): MutableMap<Int, List<Int>> {
        val rulesMap = mutableMapOf<Int, List<Int>>()
        orderingRulesInput.asSequence()
            .forEach { ruleInput ->
                ruleInput.split("|").let {
                    val page = it.first().toInt()
                    val beforeRule = it.last().toInt()
                    val pageRules = rulesMap[page] ?: emptyList()
                    rulesMap[page] = pageRules + listOf(beforeRule)
                }
            }
        return rulesMap
    }

    /**
     * K: page number V: index position
     */
    fun getUpdatePagesIndexMap(pagesInput: List<String>): MutableList<Map<Int, Int>> {
        val pageMaps = mutableListOf<Map<Int, Int>>()
        pagesInput.map {
            it.split(",")
        }.forEach { updatePages ->
            val pageMap = mutableMapOf<Int, Int>()
            updatePages
                .map { it.toInt() }
                .forEachIndexed { index, s ->
                    pageMap[s] = index
                }
            pageMaps += pageMap
        }
        return pageMaps
    }

    fun walkTroughUpdatesRules(
        updatePagesIndexMap: MutableList<Map<Int, Int>>,
        rulesMap: MutableMap<Int, List<Int>>,
        onRule: (isCorrect: Boolean, update: List<Int>) -> Unit
    ) {
        for (updatePages in updatePagesIndexMap) {
            var appliesRules = true
            updatePages.forEach { (page, index) ->
                // checking rules for updates page
                val beforeRules = rulesMap[page] ?: emptyList()

                beforeRules.forEach { beforePage ->
                    val beforePageIndex = updatePages[beforePage] ?: -1
                    if (beforePageIndex >= 0) {
                        if (index > beforePageIndex) appliesRules = false
                    }
                }
            }

            onRule(appliesRules, updatePages.map { it.key })
        }
    }

    fun getInputs(input: List<String>) =
        input.filterNot { it.isEmpty() }
            .partition {
                it.contains("|")
            }

    fun part1(input: List<String>): Int {
        val (orderingRulesInput, pagesInput) = getInputs(input)
        val rulesMap = getRulesMap(orderingRulesInput)
        val updatePagesIndexMap = getUpdatePagesIndexMap(pagesInput)

        val correctUpdates = mutableListOf<List<Int>>()
        walkTroughUpdatesRules(updatePagesIndexMap, rulesMap) { isCorrect: Boolean, update: List<Int> ->
            if (isCorrect) correctUpdates += update
        }

        return correctUpdates.sumOf {
            it[it.lastIndex / 2]
        }
    }

    fun part2(input: List<String>): Int {
        val (orderingRulesInput, pagesInput) = getInputs(input)
        val rulesMap = getRulesMap(orderingRulesInput)
        val updatePagesIndexMap = getUpdatePagesIndexMap(pagesInput)

        val incorrectUpdates = mutableListOf<MutableList<Int>>()
        walkTroughUpdatesRules(updatePagesIndexMap, rulesMap) { isCorrect: Boolean, update: List<Int> ->
            if (!isCorrect) incorrectUpdates += update.toMutableList()
        }
        val correctUpdates = mutableListOf<List<Int>>()

        for (incorrectUpdate in incorrectUpdates) {
            "== update == $incorrectUpdate".println()
            var sortedUpdate = buildList { addAll(incorrectUpdate) }.toMutableList()
            for (i in incorrectUpdate.indices) {
                val pageValue = incorrectUpdate[i]
                val pageIndex = sortedUpdate.indexOf(pageValue)

                val rulesForPage = rulesMap[pageValue] ?: emptyList()

                if (rulesForPage.isEmpty()) {
                    "no rules for $pageValue".println()
                    continue
                } else {
                    "rules for $pageValue $rulesForPage".println()
                }

                val minRuleIndex = rulesForPage.map {
                    sortedUpdate.indexOf(it)
                }.filter { it >= 0 }.minOrNull()

                if (minRuleIndex == null || minRuleIndex - 1 == pageIndex) {
                    "no swap for $pageValue".println()
                } else {
                    "goint to swap for $pageValue min rule idx $minRuleIndex".println()
                    "swaping".println()
                    val value = sortedUpdate[minRuleIndex]
                    sortedUpdate.remove(pageValue)
                    val idx = sortedUpdate.indexOf(value)
                    sortedUpdate.add(idx, pageValue)
                    "swapped $sortedUpdate\n".println()
                }

            }
            correctUpdates.add(sortedUpdate)
        }

        correctUpdates.onEach(::println)
        return correctUpdates.sumOf {
            it[it.lastIndex / 2]
        }
    }


    part1(readTestInputForDay(5, part = 1)).also {
        check(it == 143) { "test result was $it" }
    }
    part2(readTestInputForDay(5, part = 1)).also {
        check(it == 123) { "test result was $it" }
    }

    val input = readInputForDay(5)
    measureTimeMillis {
        "Part 1: ${part1(input)}".println() // Result: 6242
    }.also { "took $it ms".println() } // 9ms

    measureTimeMillis {
        "Part 2: ${part2(input)}".println() // Result: 4641
    }.also { "took $it ms".println() } // 10ms
}
