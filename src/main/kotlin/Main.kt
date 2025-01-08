package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.cross1
    println("Row hints: ${cross.rowHints}")
    println("Col hints: ${cross.colHints}")
    println("Grid:")
    cross.initialStep()
    println(cross.grid)
    for (variation in cross.generateRowVariations(3)) {
        println(variation)
    }
}
