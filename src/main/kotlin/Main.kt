package org.example

fun main() {
    val cross =
        JCross(
            listOf(
                listOf(3, 3, 4, 3, 2),
//                listOf(4, 3),
//                listOf(1, 4, 3),
//                listOf(10),
            ),
            listOf(
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
                listOf(),
            ),
        )
    println("Row hints: ${cross.rowHints}")
    println("Col hints: ${cross.colHints}")
    println("Grid:")
    cross.initialStep()
    println(cross.grid)
    println("Hello World!")
}
