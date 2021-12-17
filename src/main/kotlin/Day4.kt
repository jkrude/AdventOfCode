fun importReport(): Pair<List<Int>, List<Board>>? {
    val file = {}.javaClass.getResource("day4.txt")?.readText() ?: return null
    val split = file.split("\n\n")
    val inputNums: List<Int> = split[0].split(",").map { Integer.parseInt(it) }
    val boards: MutableList<Board> = ArrayList()
    for (idx in 1 until split.size) {
        val boardString = split[idx]
        val rows: MutableList<MutableList<Int>> =
            (boardString.split("\n") as ArrayList).map { row ->
                row.split(" ").filter { it != "" }
            } // split rows to cells
                .map { list -> list.map { Integer.parseInt(it) }.toMutableList() }.toMutableList()
        val columns: MutableList<MutableList<Int>> =
            rows.indices.map { i -> (rows.indices).map { j -> rows[j][i] }.toMutableList() }.toMutableList()
        boards.add(Board(rows, columns))
    }
    return inputNums to boards
}

data class Board(val rows: MutableList<MutableList<Int>>, val columns: MutableList<MutableList<Int>>) {

    fun remove(i: Int): Boolean {
        rows.forEach { row -> row.remove(i) }
        columns.forEach { col -> col.remove(i) }
        return rows.any { it.isEmpty() } || columns.any { it.isEmpty() }
    }
}

fun main() {
    val (nums, boards) = importReport() ?: return
    val wonAt: MutableMap<Int, Int> = HashMap()
    for ((numIdx, num) in nums.withIndex()) {
        for ((boardIdx, board) in boards.withIndex()) {
            if (boardIdx !in wonAt) {
                if (board.remove(num)) {
                    wonAt[boardIdx] = numIdx
                }

            }
        }
    }
    val (key, value) = wonAt.maxByOrNull { (_, value) -> value } ?: return
    println("Board: $key won last after $value numbers")
    println("Result is: ${boards[key].rows.flatten().sum() * nums[value]}")
    return
}