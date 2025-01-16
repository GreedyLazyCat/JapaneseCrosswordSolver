package org.example

class TempHardcodeCrosses {
    val cross1 =
        JCross(
            listOf(
                listOf(1),
                listOf(1, 2),
                listOf(1),
                listOf(2, 1),
                listOf(1),
            ),
            listOf(
                listOf(1),
                listOf(2, 1),
                listOf(1),
                listOf(1, 2),
                listOf(1),
            ),
        )
    val snowMan =
        JCross(
            listOf(
                listOf(1, 4),
                listOf(1, 1, 3, 1),
                listOf(1, 1, 1),
                listOf(2, 1, 2, 2),
                listOf(2, 1, 1, 1),
                listOf(1, 3),
                listOf(1, 2, 1, 2),
                listOf(2, 1, 1, 1),
                listOf(1, 1, 1, 1),
                listOf(1, 3),
                listOf(1, 1, 1),
                listOf(1, 1, 1),
                listOf(1, 1, 1),
                listOf(3),
            ),
            listOf(
                listOf(1, 1),
                listOf(3),
                listOf(2, 6),
                listOf(1, 1, 3),
                listOf(1),
                listOf(3, 3, 3),
                listOf(2, 1, 1, 1, 1),
                listOf(2, 3, 2, 1),
                listOf(2, 1, 1, 1, 1),
                listOf(1, 3, 3, 3),
                listOf(1, 1),
                listOf(1),
            ),
        )
    val croco =
        JCross(
            listOf(
                listOf(4),
                listOf(2),
                listOf(5),
                listOf(3, 3),
                listOf(4),
                listOf(4, 1),
            ),
            listOf(
                listOf(1, 1),
                listOf(1, 1),
                listOf(1, 2, 1),
                listOf(1, 1, 2),
                listOf(1, 3),
                listOf(6),
                listOf(4),
            ),
        )
    val teaPot =
        JCross(
            listOf(
                listOf(3, 1),
                listOf(1, 1, 2),
                listOf(1, 1, 1),
                listOf(6, 1),
                listOf(2, 4),
                listOf(1, 2, 1, 1),
                listOf(1, 3, 1),
                listOf(2, 1),
                listOf(6),
            ),
            listOf(
                listOf(4),
                listOf(4, 2),
                listOf(1, 1, 2, 1),
                listOf(1, 1, 2, 1),
                listOf(1, 1, 1, 1),
                listOf(3, 1),
                listOf(2, 1),
                listOf(2, 1),
                listOf(2, 1, 1),
                listOf(2, 3),
            ),
        )
    val teaPotSolution =
        listOf(
            mutableListOf(0, 0, 1, 1, 1, 0, 0, 0, 0, 1),
            mutableListOf(0, 1, 0, 0, 0, 1, 0, 0, 1, 1),
            mutableListOf(0, 1, 0, 0, 0, 1, 0, 0, 1, 0),
            mutableListOf(0, 1, 1, 1, 1, 1, 1, 0, 0, 1),
            mutableListOf(1, 1, 0, 0, 0, 0, 1, 1, 1, 1),
            mutableListOf(1, 0, 1, 1, 0, 0, 0, 1, 0, 1),
            mutableListOf(1, 0, 1, 1, 1, 0, 0, 0, 1, 0),
            mutableListOf(1, 1, 0, 0, 0, 0, 0, 1, 0, 0),
            mutableListOf(0, 1, 1, 2, 2, 2, 1, 0, 0, 0),
        )
}
