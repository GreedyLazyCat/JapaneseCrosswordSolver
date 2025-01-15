package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.cross6
    cross.initialStep()
    cross.solveWithEnumeration()
    println(cross)
}
