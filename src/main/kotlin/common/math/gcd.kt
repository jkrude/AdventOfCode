package common.math

fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    return gcd(b, a % b)
}

fun lcm(a: Int, b: Int): Int {
    return (a * b) / gcd(a, b)
}

fun gcd(vararg numbers: Int): Int =
    numbers.reduce { x: Int, y: Int -> gcd(x, y) }

fun lcm(vararg numbers: Int): Int =
    numbers.reduce { x: Int, y: Int -> x * (y / gcd(x, y)) }