package org.example

import java.util.Stack

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
    var grid: Array<IntArray> = Array(rowHints.size) { IntArray(colHints.size) { 0 } }

    val colLength: Int
        get() = rowHints.size

    val rowLength: Int
        get() = colHints.size

    fun isRowValid(
        row: IntArray,
        rowHint: List<Int>,
    ): Boolean {
        val gridRowLength = row.size
        var hintSet = false
        val figures = mutableListOf<Int>()

        for (i in 0..<gridRowLength) {
            if (!hintSet && row[i] > 0) {
                figures.add(0)
                hintSet = true
            }
            if (hintSet && row[i] <= 0) {
                hintSet = false
            }
            if (hintSet) {
                figures[figures.lastIndex] += 1
            }
        }

        return figures == rowHint
    }

    /**
     * Здесь пробую другой подход, нежели чем в isRowValid. Здесь воспользуюсь списками,
     * надеюсь сильно не увеличит сложность алгоритма.
     * P.s. Проверка стобца на правильность - тоже не простая задача. Реально проверить можно
     * только грубые ошибки, например, фигуры 3, а в подсказках 2 значение - сразу нарушение.
     */
    fun isColValid(
        col: Int,
        gridToValidate: Array<IntArray>,
        colHint: List<Int>,
        checkLength: Int,
    ): Validity {
//        val gridColLength = gridToValidate.size
        var hintSet = false
        val figures = mutableListOf<Int>()

        for (row in 0..checkLength) {
            if (!hintSet && gridToValidate[row][col] > 0) {
                figures.add(0)
                hintSet = true
            }
            if (hintSet && gridToValidate[row][col] <= 0) {
                hintSet = false
            }
            if (hintSet) {
                figures[figures.lastIndex] += 1
            }
        }
        if (figures.size > colHint.size) {
            return Validity.Violated
        }
        if (figures == colHint) {
            return Validity.Solved
        }
        if (figures.isNotEmpty() && figures.max() > colHint.max()) {
            return Validity.Violated
        }

        return Validity.NotViolated
    }

    fun gridColsValidity(
        workingGrid: Array<IntArray>,
        workingColHints: List<List<Int>>,
        checkLength: Int,
    ): Validity {
        var colsValidity = Validity.Solved
        for (i in workingColHints.indices) {
            var validity = isColValid(i, workingGrid, workingColHints[i], checkLength)
            if (validity == Validity.Violated) {
                return validity
            }
            if (validity != Validity.Solved) {
                colsValidity = validity
            }
        }
        return colsValidity
    }

    fun colorFullRow(row: Int) {
        for (col in 0..<grid[row].size) {
            grid[row][col] = 2
        }
    }

    fun colorFullCol(col: Int) {
        for (row in 0..<colLength) {
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
//            if (minSpace < rowLength && minSpace > (rowLength - (rowHint.maxOrNull() ?: 0))) {
//                colorRowOverlaps(i, rowHint, hintSum)
//                continue
//            }
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
        row: IntArray,
        rowHint: List<Int>,
        placements: List<Int>,
    ): IntArray {
        val result = row.copyOf()
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

    /**
     * Генерирует все возможные варианты строки. Идея основана на комбинаторной задаче.
     * Вычисляется количество свободных пробелов, а потом вычисляются все возможные перестановки
     * этих пробелов между блоками.
     */
    fun generateRowVariations(
        rowIndex: Int,
        workingGrid: Array<IntArray>,
        initRowHint: List<Int>,
    ): Sequence<Pair<Int, IntArray>> {
        val rowHint = initRowHint.toMutableList()
        val row = workingGrid[rowIndex]
        val spaceCount = row.size - rowHint.sum() - (rowHint.size - 1) // Кол-во свободных пробелов
        val spacePlacements =
            buildList<Int> {
                for (i in 0..<(rowHint.size)) {
                    add(0)
                }
                for (i in 0..<spaceCount) {
                    add(1)
                }
            }.permutations().toSet()

        return sequence {
            for (spacePlacement in spacePlacements) {
                var colorPlacement = 0
                var hintIndex = -1
                val colorPlacements = mutableListOf<Int>()
                for (placement in spacePlacement) {
                    if (placement == 1) {
                        colorPlacement += 1
                        continue
                    }
                    var toAdd = 0
                    if (hintIndex >= 0) {
                        toAdd = rowHint[hintIndex] + 1
                    }
                    colorPlacement += toAdd
                    colorPlacements.add(colorPlacement)
                    hintIndex += 1
                }
                val coloredRow = colorRowByHintAndPlacement(row, rowHint, colorPlacements)
                if (isRowValid(coloredRow, rowHint)) {
                    yield(Pair(rowIndex, coloredRow))
                }
            }
        }
    }

    fun solveWithEnumeration() {
        var workingGrid = grid.map { it.copyOf() }.toTypedArray() // Копирую матрицу для сохранности данных
        val stack = Stack<Pair<Int, IntArray>>()
        stack.addAll(generateRowVariations(0, workingGrid, rowHints[0]))
        while (stack.isNotEmpty()) {
            val (rowIndex, variation) = stack.pop()
            workingGrid = resetGrid(grid, workingGrid, rowIndex)
            workingGrid[rowIndex] = variation

            val validity = gridColsValidity(workingGrid, colHints, checkLength = rowIndex)

            if (validity == Validity.Violated) {
                continue
            }
            if (rowIndex == workingGrid.lastIndex) {
//                println("Validity $validity")
//                printGrid(workingGrid)

                if (validity == Validity.Solved) {
                    grid = workingGrid
                    break
                } else {
                    continue
                }
            }
            stack.addAll(generateRowVariations(rowIndex + 1, workingGrid, rowHints[rowIndex + 1]))
        }
    }

    fun printGrid(gridToPrint: Array<IntArray>) {
        var result = ""
        for (row in gridToPrint) {
            var rowStr = ""
            for (elem in row) {
                rowStr += " ${if (elem > 0) "*" else "."} "
            }
            rowStr += "\n"
            result += rowStr
        }
        println(result)
    }

    override fun toString(): String {
        var result = "Row hints: $rowHints\nCol hints: ${colHints}\n"

        return result
    }
}
