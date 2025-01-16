package org.example

import com.fasterxml.jackson.core.JsonParseException
import java.io.File
import java.io.FileNotFoundException

fun solveCrossword() {
    print("Enter file path: ")
    val path = readln()
    try {
        val cross = JCrossFactory().fromJsonFile(path)
        cross.initialStep()

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
    val path = readln()
    print("Enter row count: ")
    val rowCount = readln().toIntOrNull() ?: return
    print("Enter col count: ")
    val colCount = readln().toIntOrNull() ?: return
    val file = File(path)
    val cross = JCrossFactory().generateCross(rowCount, colCount)
}

fun printGreeting() {
    println("Choose variant:")
    println("1. Solve crossword from file")
    println("2. Generate random crossword")
    print("Any other input - exit:")
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
