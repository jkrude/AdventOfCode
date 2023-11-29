package common

import com.microsoft.z3.*


/* Kontext because you know kotlin */
class Kontext : Context() {


    /* Primitive math */


    /* Plus */
    infix operator fun <R : ArithSort> ArithExpr<out R>.plus(other: ArithExpr<out R>): ArithExpr<R> = mkAdd(this, other)
    infix operator fun <R : IntSort> ArithExpr<out R>.plus(other: Int): ArithExpr<IntSort> = mkAdd(this, mkInt(other))
    infix operator fun <R : IntSort> ArithExpr<out R>.plus(other: Long): ArithExpr<IntSort> = mkAdd(this, mkInt(other))
    infix operator fun <T : IntSort> Int.plus(other: Expr<T>): ArithExpr<IntSort> = mkAdd(mkInt(this), other)
    infix operator fun <T : IntSort> Long.plus(other: Expr<T>): ArithExpr<IntSort> = mkAdd(mkInt(this), other)

    /* Minus */
    infix operator fun <T : ArithSort> Expr<T>.minus(other: Expr<T>): ArithExpr<T> = mkSub(this, other)
    infix operator fun <T : IntSort> Expr<T>.minus(other: Int): ArithExpr<IntSort> = mkSub(this, mkInt(other))
    infix operator fun <T : IntSort> Expr<T>.minus(other: Long): ArithExpr<IntSort> = mkSub(this, mkInt(other))
    infix operator fun <T : IntSort> Int.minus(other: Expr<T>): ArithExpr<IntSort> = mkSub(mkInt(this), other)
    infix operator fun <T : IntSort> Long.minus(other: Expr<T>): ArithExpr<IntSort> = mkSub(mkInt(this), other)

    /* Times */
    infix operator fun <T : ArithSort> Expr<T>.times(other: Expr<T>): ArithExpr<T> = mkMul(this, other)
    infix operator fun <T : IntSort> Expr<T>.times(other: Int): ArithExpr<IntSort> = mkMul(this, mkInt(other))
    infix operator fun <T : IntSort> Expr<T>.times(other: Long): ArithExpr<IntSort> = mkMul(this, mkInt(other))
    infix operator fun <T : IntSort> Int.times(other: Expr<T>): ArithExpr<IntSort> = mkMul(mkInt(this), other)
    infix operator fun <T : IntSort> Long.times(other: Expr<T>): ArithExpr<IntSort> = mkMul(mkInt(this), other)

    /* Div */
    infix operator fun <T : ArithSort> Expr<T>.div(other: Expr<T>): ArithExpr<T> = mkDiv(this, other)
    infix operator fun <T : IntSort> Expr<T>.div(other: Int): ArithExpr<IntSort> = mkDiv(this, mkInt(other))
    infix operator fun <T : IntSort> Expr<T>.div(other: Long): ArithExpr<IntSort> = mkDiv(this, mkInt(other))
    infix operator fun <T : IntSort> Int.div(other: Expr<T>): ArithExpr<IntSort> = mkDiv(mkInt(this), other)
    infix operator fun <T : IntSort> Long.div(other: Expr<T>): ArithExpr<IntSort> = mkDiv(mkInt(this), other)

    /* Mod (always returns an int expression) */
    infix operator fun Expr<IntSort>.rem(other: Expr<IntSort>): IntExpr = mkMod(this, other)
    infix operator fun Expr<IntSort>.rem(other: Int): IntExpr = mkMod(this, mkInt(other))
    infix operator fun Expr<IntSort>.rem(other: Long): IntExpr = mkMod(this, mkInt(other))
    infix operator fun Int.rem(other: Expr<IntSort>): IntExpr = mkMod(mkInt(this), other)
    infix operator fun Long.rem(other: Expr<IntSort>): IntExpr = mkMod(mkInt(this), other)


    /* Boolean operators */

    infix fun Expr<BoolSort>.and(other: Expr<BoolSort>): BoolExpr = mkAnd(this, other)
    infix fun Expr<BoolSort>.or(other: Expr<BoolSort>): BoolExpr = mkOr(this, other)
    infix fun Expr<BoolSort>.implies(other: Expr<BoolSort>): BoolExpr = mkImplies(this, other)

    fun Iterable<Expr<BoolSort>>.all(): Expr<BoolSort> =
        this.reduce { x: Expr<BoolSort>, y: Expr<BoolSort> -> mkAnd(x, y) }

    fun <T> Iterable<T>.all(transform: (T) -> Expr<BoolSort>): BoolExpr = map(transform).all()
    fun Collection<Expr<BoolSort>>.all(): BoolExpr = mkAnd(*this.toTypedArray())

    fun Iterable<Expr<BoolSort>>.any(): Expr<BoolSort> =
        this.reduce { x: Expr<BoolSort>, y: Expr<BoolSort> -> mkOr(x, y) }

    fun <T> Iterable<T>.any(transform: (T) -> Expr<BoolSort>): Expr<BoolSort> = this.map(transform).any()

    fun Collection<Expr<BoolSort>>.any(): BoolExpr = mkOr(*this.toTypedArray())
    operator fun Expr<BoolSort>.not(): BoolExpr = mkNot(this)


    /* Comparisons */

    /* Greater than */
    infix fun <T : ArithSort> Expr<T>.gt(other: Expr<T>): BoolExpr = mkGt(this, other)
    infix fun <T : ArithSort> Expr<T>.gt(other: Int): BoolExpr = mkGt(this, mkInt(other))
    infix fun <T : ArithSort> Expr<T>.gt(other: Long): BoolExpr = mkGt(this, mkInt(other))
    infix fun <T : ArithSort> Int.gt(other: Expr<T>): BoolExpr = mkGt(mkInt(this), other)
    infix fun <T : ArithSort> Long.gt(other: Expr<T>): BoolExpr = mkGt(mkInt(this), other)

    /* Greater or equal */
    infix fun <T : ArithSort> Expr<T>.ge(other: Expr<T>): BoolExpr = mkGe(this, other)
    infix fun <T : ArithSort> Expr<T>.ge(other: Int): BoolExpr = mkGe(this, mkInt(other))
    infix fun <T : ArithSort> Expr<T>.ge(other: Long): BoolExpr = mkGe(this, mkInt(other))
    infix fun <T : ArithSort> Int.ge(other: Expr<T>): BoolExpr = mkGe(mkInt(this), other)
    infix fun <T : ArithSort> Long.ge(other: Expr<T>): BoolExpr = mkGe(mkInt(this), other)

    /* Less than */
    infix fun <T : ArithSort> Expr<T>.lt(other: Expr<T>): BoolExpr = mkLt(this, other)
    infix fun <T : ArithSort> Expr<T>.lt(other: Int): BoolExpr = mkLt(this, mkInt(other))
    infix fun <T : ArithSort> Expr<T>.lt(other: Long): BoolExpr = mkLt(this, mkInt(other))
    infix fun <T : ArithSort> Int.lt(other: Expr<T>): BoolExpr = mkLt(mkInt(this), other)
    infix fun <T : ArithSort> Long.lt(other: Expr<T>): BoolExpr = mkLt(mkInt(this), other)

    /* Less or equal  */
    infix fun <T : ArithSort> Expr<T>.le(other: Expr<T>): BoolExpr = mkLe(this, other)
    infix fun <T : ArithSort> Expr<T>.le(other: Int): BoolExpr = mkLe(this, mkInt(other))
    infix fun <T : ArithSort> Expr<T>.le(other: Long): BoolExpr = mkLe(this, mkInt(other))
    infix fun <T : ArithSort> Int.le(other: Expr<T>): BoolExpr = mkLe(mkInt(this), other)
    infix fun <T : ArithSort> Long.le(other: Expr<T>): BoolExpr = mkLe(mkInt(this), other)

    /* Equals */
    infix fun <T : Sort> Expr<T>.eq(other: Expr<T>): BoolExpr = mkEq(this, other)
    infix fun <T : Sort> Expr<T>.eq(other: Int): BoolExpr = mkEq(this, mkInt(other))
    infix fun <T : Sort> Expr<T>.eq(other: Long): BoolExpr = mkEq(this, mkInt(other))

    val Int.z3: IntNum
        get() = mkInt(this)

    fun Solver.assertUniqueNumbers(
        symbols: List<IntExpr>,
        from: Int = 0,
        toExclusive: Int = symbols.size
    ) {
        require(toExclusive - from >= symbols.size)

        symbols.forEach { add((it ge from) and (it lt toExclusive)) }
        symbols.forEachIndexed { index, intExpr ->
            add(
                symbols.indices.all { otherIdx ->
                    // either the index is the same or they are not the same number
                    (index.z3 eq otherIdx) or !(intExpr eq symbols[otherIdx])
                }
            )
        }
    }

}


fun Expr<IntSort>.toIntOrNull() = if (this.isIntNum) (this as IntNum).int else null
fun Expr<IntSort>.toInt() = this.toIntOrNull() ?: throw IllegalArgumentException("$this")
fun Expr<IntSort>.toLongOrNull() = if (this.isIntNum) (this as IntNum).int64 else null
fun Expr<IntSort>.toLong() = this.toLongOrNull() ?: throw IllegalArgumentException("$this")

