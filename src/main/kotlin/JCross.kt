package org.example

class JCross(
    val rowHints: List<List<Int>>,
    val colHints: List<List<Int>>,
) {
    /**
     * 0 - не закрашен
     * 1 - закрашен
     *
     * 2 - однозначно закрашен
     * -1 - однозначно не закрашен
     */
    val grid: List<MutableList<Int>> = List(rowHints.size) { MutableList(colHints.size) { 0 } }

    val colLength: Int
        get() = rowHints.size

    val rowLength: Int
        get() = colHints.size

    fun colorFullRow(row: Int) {
        for (col in 0..<grid[row].size) {
            grid[row][col] = 2
        }
    }

    fun colorAllPersistentRowHints(row: Int) {
        val rowHint = rowHints[row]
        var start = 0
        for (hint in rowHint) {
            for (i in start..<(start + hint)) {
                grid[row][i] = 2
            }
            start += hint + 1
        }
    }

    fun colorRowOverlaps(
        row: Int,
        rowHint: List<Int>,
        hintSum: Int,
    ) {
        val maxHint = rowHint.max()
        val minSpace = hintSum + rowHint.size - 1 // Мин кол-во всех "занятых" клеток вместе с пробелами
        val freeSpace = rowLength - minSpace // Кол-во пустых клеток для каждой подсказки
        var start = 0 // Старт откуда закрашивать
        for (hint in rowHint) {
            val colorStart = start + freeSpace
            val colorEnd = start + hint - 1
            println("For hint $hint: ColorStart ${colorStart + 1} ColorEnd ${colorEnd + 1} Start $start FreeSpace $freeSpace")
            colorRangeInRow(row, colorStart, colorEnd)
            start = colorStart + hint - 1
        }
    }

    fun colorRangeInRow(
        row: Int,
        colorStart: Int,
        colorEnd: Int,
    ) {
        val gridRow = grid[row]
        for (i in colorStart..colorEnd) {
            gridRow[i] = 2
        }
    }

    /**
     * Предобработка кроссворда. Закрашивание тех клеток, которые могут быть однозначно закранешы.
     * Однозначно закрашенные квадраты помечаются 2.
     */
    fun initialStep() {
        for ((i, rowHint) in rowHints.withIndex()) {
            val hintSum = rowHint.sum()
            /* -- Обработка однозначных ситуаций --
             * Кол-во закрашенных клеток в таких ситуациях + кол-во пробелов всегда равно сумме
             * всех подсказок - 1
             * */
            val minSpace = hintSum + rowHint.size - 1
            if (rowHint.size == 1 && rowHint[0] == rowLength) {
                colorFullRow(i)
                continue
            }
            if (minSpace == rowLength) {
                colorAllPersistentRowHints(i)
                continue
            }
//            print("Minspace $minSpace rowlength $rowLength sub ${rowLength - rowHint.max()}")
            if (minSpace < rowLength && minSpace > (rowLength - rowHint.max())) {
                colorRowOverlaps(i, rowHint, hintSum)
                continue
            }
            // -------
        }
        for ((i, colHint) in colHints.withIndex()) {
            val hintSum = colHint.sum()
        }
    }
}
