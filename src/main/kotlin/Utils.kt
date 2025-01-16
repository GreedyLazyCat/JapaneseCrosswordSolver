package org.example

fun resetGrid(
    fromGrid: Array<IntArray>,
    toGrid: Array<IntArray>,
    fromIndex: Int,
): Array<IntArray> {
    for (i in fromIndex..<fromGrid.size) {
        toGrid[i] = fromGrid[i]
    }
    return toGrid
}

val <T> List<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = drop(1)

/**
 * Честно конкретно это не моя функция она взята с гитхаба.
 * В любом случае я тут воспользовался бы библиотекой.
 * Функция ниже - самый быстрый вариант.
 * То что сам пробовал реализовывать отдельно по скорости не сравниться.
 * Лучше я не сделаю.
 * Используя другие варианты кроссворд tea_pot решается за более чем 7 минут.
 * С этим - за минуту.
 * Так что генерация перестановок тоже значительно влияет на ускорение и надеюсь, что это
 * оправдает использование сторонней функции.
 */
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
