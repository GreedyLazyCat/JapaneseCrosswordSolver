package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.teaPot
//    println("Validity: ${cross.gridColsValidity(cross.grid, cross.colHints)}")
    cross.initialStep()
    cross.tempPrintGrid(cross.grid)
    for (variation in cross.generateRowVariations(2, cross.grid, cross.rowHints[2])) {
        println(variation)
    }
//    cross.solveWithEnumeration()
//        println(cross)
}
