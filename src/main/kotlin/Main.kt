package org.example

fun main() {
    val crosses = TempHardcodeCrosses()
    val cross = crosses.teaPot
//    cross.initialStep()
    cross.solveWithEnumeration()
    println(cross)
}
