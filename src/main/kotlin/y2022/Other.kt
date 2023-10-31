package y2022

typealias ComplexResult = Pair<Boolean, Boolean>

fun complexResult(rightOrder: Boolean, finalCompare: Boolean) = rightOrder to finalCompare

fun wrongOrder() = complexResult(rightOrder = false, false)
fun finallyRight() = complexResult(rightOrder = true, true)