package org.example

fun addValueToRange(
    from: Int,
    to: Int,
    value: Int,
    list: List<Int>,
): MutableList<Int> {
    val copy = list.toMutableList()
    for (i in from..to) {
        copy[i] += value
    }
    return copy
}

fun tempCompareGrids(
    A: List<MutableList<Int>>,
    B: List<MutableList<Int>>,
): Boolean {
    for (i in A.indices) {
        for (j in B.indices) {
            if (A[i][j] == 0 && B[i][j] != 0)
                {
                    return false
                }
            if (A[i][j] > 0 && B[i][j] < 0)
                {
                    return false
                }
            if (A[i][j] < 0 && B[i][j] > 0)
                {
                    return false
                }
        }
    }
    return true
}

val <T> List<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = drop(1)

// Extension function

fun <T> List<T>.permutations(): List<List<T>> {
    if (isEmpty()) return emptyList()
    if (size == 1) return listOf(this)

    return tail
        .permutations()
        .fold(mutableListOf()) { acc, perm ->
            (0..perm.size).mapTo(acc) { i ->
                perm.subList(0, i) + head + perm.subList(i, perm.size)
            }
        }
}
