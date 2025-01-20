package org.example

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jsonMapper
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths

fun solveCrossword() {
    print("Enter file path: ")
    var path = readln()
    if (!Paths.get(path).isAbsolute) {
        path = Paths.get(path).toAbsolutePath().toString()
    }
    try {
        val cross = JCrossFactory().fromJsonFile(path)
        cross.initialStep()
        println("Hints")
        println(cross)
        println("Grid on initial Step: ")
        cross.printGrid(cross.grid)

        cross.solveWithEnumeration()

        println("Grid after solving: ")
        cross.printGrid(cross.grid)
    } catch (e: FileNotFoundException) {
        println("File not found")
    } catch (e: JsonParseException) {
        println(e.message)
    }
}

fun crosswordGeneration() {
    print("Enter save file path: ")
    var path = readln()
    if (!Paths.get(path).isAbsolute) {
        path = Paths.get(path).toAbsolutePath().toString()
    }
    print("Enter row count: ")
    val rowCount = readln().toIntOrNull() ?: return
    print("Enter col count: ")
    val colCount = readln().toIntOrNull() ?: return
    val file = File(path)
    val cross = JCrossFactory().generateCross(rowCount, colCount)
    val mapper = jsonMapper()
    val node = mapper.createObjectNode()
    val rowHintArrayNode = mapper.createArrayNode()
    for (rowHint in cross.rowHints) {
        val hintNode = mapper.createArrayNode()
        for (value in rowHint) {
            hintNode.add(value)
        }
        rowHintArrayNode.add(hintNode)
    }
    val colHintArrayNode = mapper.createArrayNode()
    for (colHint in cross.colHints) {
        val hintNode = mapper.createArrayNode()
        for (value in colHint) {
            hintNode.add(value)
        }
        colHintArrayNode.add(hintNode)
    }
    node.putIfAbsent("row_hints", rowHintArrayNode)
    node.putIfAbsent("col_hints", colHintArrayNode)
    file.writeText(mapper.writeValueAsString(node))
}

fun printGreeting() {
    println("Choose variant:")
    println("1. Solve crossword from file")
    println("2. Generate random crossword")
    print("Any other input - exit: ")
    val input = readln().toIntOrNull() ?: return
    if (input == 1) {
        solveCrossword()
    } else if (input == 2) {
        crosswordGeneration()
    }
}

fun main() {
    printGreeting()
}
