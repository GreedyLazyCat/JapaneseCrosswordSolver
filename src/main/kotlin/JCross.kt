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

    fun isRowValid(
        row: Int,
        gridToValidate: List<MutableList<Int>>,
        rowHint: List<Int>,
    ): Boolean {
        val gridRowLength = gridToValidate.first().size
        val rowHintCopy = rowHint.toMutableList()
        var currentHint = rowHintCopy.removeFirst()
        var hintSet = grid[row].first() > 0

        /*
            Проверка, что закрашенные квадраты в принципе подходят под подсказки
            Это нужно для отсеивания вариантов, которые проверяются на уже заполненных квадратах
         */
        for (col in 0..<gridRowLength) {
//            println("Current Hint $currentHint value ${grid[row][col]} hint set $hintSet")
            if (hintSet && currentHint == 0 && grid[row][col] <= 0) {
                if (rowHintCopy.isEmpty()) {
                    break
                }
                hintSet = false
                currentHint = rowHintCopy.removeFirst()
                continue
            }
            if (!hintSet && grid[row][col] > 0) {
                hintSet = true
            }
            if (grid[row][col] > 0 && hintSet) {
                currentHint -= 1
            }
            if (grid[row][col] > 0 && currentHint < 0 && hintSet) {
                return false
            }
            if (grid[row][col] <= 0 && currentHint >= 0 && hintSet) {
                return false
            }
        }
        if (currentHint > 0) {
            return false
        }
        if (rowHintCopy.isNotEmpty()) {
            return false
        }
        return true
    }

    fun isColValid(
        col: Int,
        gridToValidate: List<MutableList<Int>>,
        colHint: List<Int>,
    ): Boolean {
        val gridColLength = gridToValidate.size
        val colHintCopy = colHint.toMutableList()
        var currentHint = colHintCopy.removeFirst()
        var hintSet = grid[0][col] > 0

        /*
            Проверка, что закрашенные квадраты в принципе подходят под подсказки
            Это нужно для отсеивания вариантов, которые проверяются на уже заполненных квадратах
         */
        for (row in 0..<gridColLength) {
            if (hintSet && currentHint == 0 && grid[row][col] <= 0) {
                if (colHintCopy.isEmpty()) {
                    break
                }
                hintSet = false
                currentHint = colHintCopy.removeFirst()
                continue
            }
            if (!hintSet && grid[row][col] > 0) {
                hintSet = true
            }
            if (grid[row][col] > 0 && hintSet) {
                currentHint -= 1
            }
            if (grid[row][col] > 0 && currentHint < 0 && hintSet) {
                return false
            }
            if (grid[row][col] <= 0 && currentHint >= 0 && hintSet) {
                return false
            }
        }
        if (currentHint > 0) {
            return false
        }
        if (colHintCopy.isNotEmpty()) {
            return false
        }
        return true
    }

    fun colorFullRow(row: Int) {
        for (col in 0..<grid[row].size) {
            grid[row][col] = 2
        }
    }

    fun colorFullCol(col: Int) {
        for (row in 0..<rowLength) {
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

    fun colorAllPersistentColHints(col: Int) {
        val colHint = colHints[col]
        var start = 0
        for (hint in colHint) {
            for (i in start..<(start + hint)) {
                grid[i][col] = 2
            }
            start += hint + 1
        }
    }

    fun colorColOverlaps(
        col: Int,
        colHint: List<Int>,
        hintSum: Int,
    ) {
        val minSpace = hintSum + colHint.size - 1 // Мин кол-во всех "занятых" клеток вместе с пробелами
        val freeSpace = colLength - minSpace // Кол-во пустых клеток для каждой подсказки
        var start = 0 // Старт откуда закрашивать
        for (hint in colHint) {
            val colorStart = start + freeSpace
            val colorEnd = start + hint - 1
            colorRangeInCol(col, colorStart, colorEnd, 2)
            start = colorEnd + 2
        }
    }

    fun colorRowOverlaps(
        row: Int,
        rowHint: List<Int>,
        hintSum: Int,
    ) {
        val minSpace = hintSum + rowHint.size - 1 // Мин кол-во всех "занятых" клеток вместе с пробелами
        val freeSpace = rowLength - minSpace // Кол-во пустых клеток для каждой подсказки
        var start = 0 // Старт откуда закрашивать
        for (hint in rowHint) {
            val colorStart = start + freeSpace
            val colorEnd = start + hint - 1
//            println("For hint $hint: ColorStart ${colorStart + 1} ColorEnd ${colorEnd + 1} Start ${start + 1} FreeSpace $freeSpace")
            colorRangeInRow(row, colorStart, colorEnd, 2)
            start = colorEnd + 2
        }
    }

    fun colorRangeInRow(
        row: Int,
        colorStart: Int,
        colorEnd: Int,
        value: Int,
    ) {
        val gridRow = grid[row]
        for (i in colorStart..colorEnd) {
            gridRow[i] = value
        }
    }

    fun colorRangeInCol(
        col: Int,
        colorStart: Int,
        colorEnd: Int,
        value: Int,
    ) {
        for (i in colorStart..colorEnd) {
            grid[i][col] = value
        }
    }

    fun calculateLeftForRow(row: Int) {
        val rowHint = rowHints[row].toMutableList()
        rowHint.reverse()
        val initPainted = mutableListOf<Int>()
        for (i in (rowLength - 1) downTo 0) {
            if (grid[row][i] > 0) {
                initPainted.add(i)
            }
        }
        val painted = initPainted.toMutableList()
        var hintIndex = 0
        var startPainted = painted.removeFirst()

        while (true) {
//            print("$painted ")
            if (grid[row][startPainted] > 0 && rowHint[hintIndex] == 0) {
                println("start painted: $startPainted hintIndex $hintIndex")
                println("row hint $rowHint")
                break
            } else if (grid[row][startPainted] <= 0 && rowHint[hintIndex] == 0) {
                hintIndex += 1
                startPainted = painted.removeFirst()
            } else {
                rowHint[hintIndex] -= 1
                startPainted -= 1
            }
        }
    }

    /**
     * Предобработка кроссворда. Закрашивание тех клеток, которые могут быть однозначно закранешы.
     * Однозначно закрашенные квадраты помечаются 2.
     */
    fun initialStep() {
        for ((i, rowHint) in rowHints.withIndex()) {
            if (rowHint.isEmpty()) {
                continue
            }
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
            if (minSpace < rowLength && minSpace > (rowLength - (rowHint.maxOrNull() ?: 0))) {
                colorRowOverlaps(i, rowHint, hintSum)
                continue
            }
            // -------
        }
        for ((i, colHint) in colHints.withIndex()) {
            if (colHint.isEmpty()) {
                continue
            }
            val hintSum = colHint.sum()

            val minSpace = hintSum + colHint.size - 1
            if (colHint.size == 1 && colHint[0] == colLength) {
                colorFullCol(i)
                continue
            }
            if (minSpace == colLength) {
                colorAllPersistentColHints(i)
                continue
            }
            if (minSpace < colLength && minSpace > (colLength - (colHint.maxOrNull() ?: 0))) {
                colorColOverlaps(i, colHint, hintSum)
                continue
            }
        }
    }

    fun colorRowByHintAndPlacement(
        row: List<Int>,
        rowHint: List<Int>,
        placements: List<Int>,
    ): MutableList<Int> {
        val result = row.toMutableList()
        for ((i, hint) in rowHint.withIndex()) {
            val placement = placements[i]
            for (j in placement..<(placement + hint)) {
                if (result[j] >= 0) {
                    result[j] = 1
                }
            }
        }
        return result
    }

    fun generateRowVariations(row: Int): List<List<Int>> {
        val rowHint = rowHints[row].toMutableList()
        val row = grid[row].toMutableList()
        val initialLeftFigPlacement =
            buildList<Int> {
                add(0)
                var prev = 0
                for ((i, hint) in rowHint.withIndex()) {
                    if (i == rowHint.size - 1) {
                        break
                    }
                    prev += hint + 1
                    add(prev)
                }
            }
        var initialRightFigPlacement = listOf<Int>()

        var curFigPlacement = initialLeftFigPlacement.toMutableList()
        var curFig = 0
        println("$initialLeftFigPlacement")
        println(colorRowByHintAndPlacement(row, rowHint, curFigPlacement))
        while (curFig < initialLeftFigPlacement.size) {
            if (curFigPlacement.last() + 1 >= row.size) {
                curFig += 1
                if (initialRightFigPlacement.isEmpty()) {
                    initialRightFigPlacement = curFigPlacement
                }
                curFigPlacement = initialLeftFigPlacement.toMutableList()
                continue
            }
            curFigPlacement = addValueToRange(curFig, curFigPlacement.size - 1, 1, curFigPlacement)
            println(curFigPlacement)
            println(colorRowByHintAndPlacement(row, rowHint, curFigPlacement))
        }
        curFigPlacement = initialRightFigPlacement.toMutableList()
        curFig = curFigPlacement.lastIndex - 1
        while (curFig >= 0) {
            if (curFigPlacement.first() - 1 < 0) {
                curFig -= 1
                curFigPlacement = initialRightFigPlacement.toMutableList()
                continue
            }
            curFigPlacement = addValueToRange(0, curFig, -1, curFigPlacement)
            println(curFigPlacement)
            println(colorRowByHintAndPlacement(row, rowHint, curFigPlacement))
        }

        return listOf()
    }
}
