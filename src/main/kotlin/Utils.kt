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
