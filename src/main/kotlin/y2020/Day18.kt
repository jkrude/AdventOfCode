package y2020

import common.readFileLines

object Day18 {

    sealed interface AST {
        fun eval(): Long
        fun plusBeforeMul(): AST
    }

    class Leaf(val value: Int) : AST {
        override fun eval(): Long = value.toLong()
        override fun plusBeforeMul(): AST = this
    }

    class Parenthesis(val inner: AST) : AST by inner {
        override fun plusBeforeMul(): AST = Parenthesis(inner.plusBeforeMul())
    }


    class Plus(val left: AST, val right: AST) : AST {
        override fun eval(): Long = left.eval() + right.eval()

        /*
        * Exchange order of evaluation by pushing down plus in the AST hierarchy.
        * Only case after parsing strictly left to right is if the left child of a Plus is a Mul
        *  x * y + z
        *      +           *
        *   *     z ->  x     +
        * x   y             y   z
        */
        override fun plusBeforeMul(): AST {
            // Transform bottom to top
            val reordered = Plus(left.plusBeforeMul(), right.plusBeforeMul())
            return if (reordered.left is Mul) {
                val newPlus = Plus(left = reordered.left.right, right = this.right)
                Mul(reordered.left.left, newPlus).plusBeforeMul()
            } else reordered
        }
    }

    class Mul(val left: AST, val right: AST) : AST {
        override fun eval(): Long = left.eval() * right.eval()
        override fun plusBeforeMul(): AST = Mul(left.plusBeforeMul(), right.plusBeforeMul())
    }

    private fun removeWhitespace(string: String): String =
        string.replace(" ", "")


    /**
     * Parse a valid expression containing '+','*',\d+,'(',')' but no whitespace.
     * Parsing without operator precedence, strictly left to right.
     * Find the next operator (+ or *)
     *  take left and greedy right of it
     *  Create the AST node and pass it to the next operator as left side
     */
    fun parseExpression(expression: String): AST {
        var left: AST? = null
        var i = 0
        while (i <= expression.lastIndex) {
            val (idx, isPlus) = findNextOp(expression, i) ?: return Leaf(expression.toInt())
            val (findRight, endIdxRight) = findRight(expression, idx + 1)
            val findLeft = left ?: findLeft(expression.substring(i, idx))
            left = if (isPlus) Plus(findLeft, findRight) else Mul(findLeft, findRight)
            i = endIdxRight // Jump after the next right side
        }
        return left!!
    }

    private fun findLeft(leftPart: String): AST {
        if (leftPart[0] == '(') {
            val closing = idxOfClosingParenthesis(leftPart, 1)
            if (closing == leftPart.lastIndex) return Parenthesis(
                parseExpression(
                    leftPart.substring(
                        1,
                        leftPart.lastIndex
                    )
                )
            )
        }
        return parseExpression(leftPart)
    }

    private fun findNextOp(expression: String, startIndex: Int): Pair<Int, Boolean>? {
        var i = startIndex
        while (i <= expression.lastIndex) {
            when (expression[i]) {
                '+', '*' -> return i to (expression[i] == '+')
                '(' -> i = idxOfClosingParenthesis(expression, startIndex + 1)
                else -> i++
            }
        }
        return null
    }

    /**
     * Search until the next operator or parse a parenthesis's clause.
     * Return the right side and the idx immediately right of the right side.
     */
    private fun findRight(expression: String, startIdx: Int): Pair<AST, Int> {
        val (endIdx, symbol) = expression.findAnyOf(listOf("*", "+", "("), startIndex = startIdx)
            ?: return Leaf(expression.substring(startIdx).toInt()) to expression.length // everything is right side
        if (symbol == "(") {
            val closingIdx = idxOfClosingParenthesis(expression, endIdx + 1)
            return Parenthesis(parseExpression(expression.substring(endIdx + 1, closingIdx))) to (closingIdx + 1)
        }
        return Leaf(expression.substring(startIdx, endIdx).toInt()) to endIdx

    }

    // Find the matching closing ')' assuming expression[start-1] is the opening '('.
    private fun idxOfClosingParenthesis(expression: String, start: Int): Int {
        var depth = 0
        var idx = start
        while (idx < expression.length) {
            when (expression[idx]) {
                ')' -> {
                    if (depth == 0) return idx
                    depth--
                }

                '(' -> depth++
            }
            idx++
        }
        error("Could not find closing ')' in $expression starting at $start")
    }

    fun partOne(lines: List<String>): Long =
        lines.map(::removeWhitespace)
            .sumOf { parseExpression(it).eval() }

    fun partTwo(lines: List<String>): Long =
        lines.map(::removeWhitespace)
            .map(::parseExpression)
            .map(AST::plusBeforeMul)
            .sumOf { it.eval() }
}

fun main() {
    println(Day18.partOne(readFileLines(18, 2020)))
    println(Day18.partTwo(readFileLines(18, 2020)))
}