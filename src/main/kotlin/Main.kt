package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.cross5
    println("Row hints: ${cross.rowHints}")
    println("Col hints: ${cross.colHints}")
    println("Grid:")
    println(cross.grid)
    cross.generateRowVariations(0)
}
