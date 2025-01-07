package org.example

fun main() {
//                listOf(4, 3),

    val crosses = TempHardcodeCrosses()
    println("Row hints: ${crosses.cross1.rowHints}")
    println("Col hints: ${crosses.cross1.colHints}")
    println("Grid:")
    crosses.cross1.initialStep()
    println(crosses.cross1.grid)
    println("Hello World!")
}
