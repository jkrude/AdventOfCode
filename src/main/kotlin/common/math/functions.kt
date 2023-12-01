package common.math

import kotlin.math.abs

fun sumTo(x: Int) = if (x > 0) x * (x + 1) / 2 else abs(x) * (abs(x) + 1) / -2