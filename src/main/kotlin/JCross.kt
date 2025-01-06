package org.example

class JCross(val rowHints: List<List<Int>>, val colHints: List<List<Int>>) {
    val grid: List<List<Int>> = List(colHints.size) { List(rowHints.size) { 0 } }
}