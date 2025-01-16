package org.example

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.FileNotFoundException
import kotlin.random.Random

class JCrossFactory {
    fun fromJsonFile(path: String): JCross {
        val file = File(path)
        if (!file.exists()) {
            throw FileNotFoundException()
        }
        val jsonObject = jacksonObjectMapper().readTree(file)
        if (!jsonObject.has("row_hints") || !jsonObject.has("col_hints")) {
            throw JsonParseException("row_hints and col_hints are required")
        }
        if (!jsonObject.get("row_hints").isArray || !jsonObject.get("col_hints").isArray) {
            throw JsonParseException("row_hints and col_hints must be an array")
        }
        val rowHintsJson = jsonObject.get("row_hints").map { elem -> elem.map { it.asInt() } }.toList()
        val colHintsJson = jsonObject.get("col_hints").map { elem -> elem.map { it.asInt() } }.toList()
        return JCross(rowHintsJson, colHintsJson)
    }

    fun generateCross(
        rowHintSize: Int,
        colHintSize: Int,
    ): JCross {
        var grid: List<MutableList<Int>> = List(rowHintSize) { MutableList(colHintSize) { 0 } }
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val randInt = Random.nextInt(100)
                if (col != 0) {
                    if (grid[row][col - 1] > 0) {
                        grid[row][col] = if (randInt > Random.nextInt(30, 40)) 1 else 0
                    } else {
                        grid[row][col] = if (randInt > 50) 1 else 0
                    }
                    continue
                }
                grid[row][col] = if (randInt > 50) 1 else 0
            }
        }

        return JCross(getRowHintsFromGrid(grid), getColHintsFromGrid(grid))
    }

    private fun getColHintsFromGrid(grid: List<List<Int>>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        for (col in 0..<grid.first().size) {
            var hintSet = false
            val figures = mutableListOf<Int>()

            for (row in grid.indices) {
                if (!hintSet && (grid[row][col] > 0)) {
                    figures.add(0)
                    hintSet = true
                }
                if (hintSet && (grid[row][col] <= 0)) {
                    hintSet = false
                }
                if (hintSet) {
                    figures[figures.lastIndex] += 1
                }
            }
            result.add(figures)
        }
        return result
    }

    private fun getRowHintsFromGrid(grid: List<List<Int>>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        for (row in grid) {
            var hintSet = false
            val figures = mutableListOf<Int>()

            for (i in row.indices) {
                if (!hintSet && row[i] > 0) {
                    figures.add(0)
                    hintSet = true
                }
                if (hintSet && row[i] <= 0) {
                    hintSet = false
                }
                if (hintSet) {
                    figures[figures.lastIndex] += 1
                }
            }
            result.add(figures)
        }

        return result
    }
}
