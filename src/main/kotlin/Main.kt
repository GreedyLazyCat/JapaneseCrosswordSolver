package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.cross5
    println("Row hints: ${cross.rowHints}")
    println("Col hints: ${cross.colHints}")
    println("Grid:")
//    cross.initialStep()
    cross.grid[0][0] = 1
    cross.grid[1][0] = 0
    cross.grid[2][0] = 0
    cross.grid[3][0] = 1
    cross.grid[4][0] = 1
    cross.grid[5][0] = 0
    cross.grid[6][0] = 1
    cross.grid[7][0] = 1
    cross.grid[8][0] = 1
    cross.grid[9][0] = 0
    println(cross.grid)
    println(cross.isColValid(0, cross.grid, cross.colHints[0]))
//    for (variation in cross.generateRowVariations(3)) {
//        println(variation)
//    }
}
