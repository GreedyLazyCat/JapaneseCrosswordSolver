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
    var grid: List<MutableList<Int>> = List(rowHints.size) { MutableList(colHints.size) { 0 } }

    val colLength: Int
        get() = rowHints.size

    val rowLength: Int
        get() = colHints.size

    fun isRowValid(
        row: List<Int>,
        rowHint: List<Int>,
    ): Boolean {
        val gridRowLength = row.size
        val rowHintCopy = rowHint.toMutableList()
        var currentHint = rowHintCopy.removeFirst()
        var hintSet = row.first() > 0

        /*
            Проверка, что закрашенные квадраты в принципе подходят под подсказки
            Это нужно для отсеивания вариантов, которые проверяются на уже заполненных квадратах
         */
        for (col in 0..<gridRowLength) {
//            println("Current Hint $currentHint value ${grid[row][col]} hint set $hintSet")
            if (hintSet && currentHint == 0 && row[col] <= 0) {
                if (rowHintCopy.isEmpty()) {
                    break
                }
                hintSet = false
                currentHint = rowHintCopy.removeFirst()
                continue
            }
            if (!hintSet && row[col] > 0) {
                hintSet = true
            }
            if (row[col] > 0 && hintSet) {
                currentHint -= 1
            }
            if (row[col] > 0 && currentHint < 0 && hintSet) {
                return false
            }
            if (row[col] <= 0 && currentHint >= 0 && hintSet) {
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

    /**
     * Здесь пробую другой подход, нежели чем в isRowValid. Здесь воспользуюсь списками,
     * надеюсь сильно не увеличит сложность алгоритма.
     * P.s. Проверка стобца на правильность - тоже не простая задача. Реально проверить можно
     * только грубые ошибки, например, фигуры 3, а в подсказках 2 значение - сразу нарушение.
     */
    fun isColValid(
        col: Int,
        gridToValidate: List<MutableList<Int>>,
        colHint: List<Int>,
    ): Validity {
        val gridColLength = gridToValidate.size
        var hintSet = false
        val figures = mutableListOf<Int>()

        for (row in 0..<gridColLength) {
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

    fun gridColsValidity(): Validity {
        var colsValidity = Validity.Solved
        for (i in 0..<colLength) {
            var validity = isColValid(i, grid, colHints[i])
            if (validity == Validity.Violated) {
                return validity
            }
            if (validity != Validity.Solved) {
                colsValidity = validity
            }
        }
        return colsValidity
    }

    fun gridColsValidity(
        workingGrid: List<MutableList<Int>>,
        workingColHints: List<List<Int>>,
    ): Validity {
        var colsValidity = Validity.Solved
        for (i in workingColHints.indices) {
            var validity = isColValid(i, workingGrid, workingColHints[i])
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

    /**
     * Генерирует все возможные варианты строки. Идея основана на комбинаторной задаче.
     * Вычисляется количество свободных пробелов, а потом вычисляются все возможные перестановки
     * этих пробелов между блоками.
     */
    fun generateRowVariations(
        rowIndex: Int,
        workingGrid: List<List<Int>>,
        initRowHint: List<Int>,
    ): Sequence<Pair<Int, List<Int>>> {
        val rowHint = initRowHint.toMutableList()
        val row = workingGrid[rowIndex].toMutableList()
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
        val workingGrid = grid.map { it.toMutableList() }.toMutableList() // Копирую матрицу для сохранности данных
        val stack = Stack<Pair<Int, List<Int>>>()
        var maxStackSize = 0
        stack.addAll(generateRowVariations(0, workingGrid, rowHints[0]))
        while (stack.isNotEmpty()) {
            val (rowIndex, variation) = stack.pop()
            workingGrid[rowIndex] = variation.toMutableList()

            val validity = gridColsValidity(workingGrid, colHints)

            if (validity == Validity.Violated) {
                workingGrid[rowIndex] = grid[rowIndex]
                continue
            }
            if (rowIndex == workingGrid.lastIndex) {
                if (validity == Validity.Solved) {
                    grid = workingGrid
                    break
                } else if (validity == Validity.NotViolated) {
                    workingGrid[rowIndex] = grid[rowIndex]
                    continue
                }
            }
            stack.addAll(generateRowVariations(rowIndex + 1, workingGrid, rowHints[rowIndex + 1]))
            if (stack.size > maxStackSize) {
                maxStackSize = stack.size
            }
        }
        println(maxStackSize)
    }

    override fun toString(): String {
        var result = "Row hints: $rowHints\nCol hints: ${colHints}\nGrid:\n"
        for (row in grid) {
            var rowStr = ""
            for (elem in row) {
                rowStr += "$elem "
            }
            rowStr += "\n"
            result += rowStr
        }
        return result
    }
}
