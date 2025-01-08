package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.cross5
    println("Row hints: ${cross.rowHints}")
    println("Col hints: ${cross.colHints}")
    println("Grid:")
    println(cross.grid)
    cross.grid[0][0] = 2
    cross.grid[0][1] = 0
    cross.grid[0][2] = 2
    cross.grid[0][3] = 2
    cross.grid[0][4] = 0
    println(cross.grid)
    println(cross.isRowValid(0, cross.grid, cross.rowHints[0], cross.colHints))
}
