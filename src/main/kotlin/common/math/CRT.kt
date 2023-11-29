package common.math

/**
 * Based on the Java-implementation from Gregory Owen
 * https://github.com/GregOwen/Chinese-Remainder-Theorem/blob/master/CRT.java
 */

fun euclidean(a: Long, b: Long): Pair<Long, Long> {
    if (b > a) {
        //reverse the order of inputs, run through this method, then reverse outputs
        return euclidean(b, a).let { (x, y) -> y to x }

    }
    val q = a / b
    //a = q*b + r --> r = a - q*b
    val r = a - q * b

    //when there is no remainder, we have reached the gcd and are done
    if (r == 0L) {
        return 0L to 1L
    }

    //call the next iteration down (b = qr + r_2)
    val next = euclidean(b, r)
    return next.second to (next.first - q * next.second)
}

//finds the least positive integer equivalent to a mod m
fun leastPosEquiv(a: Long, mod: Long): Long {
    //a is equivalent to b mod -m <==> a equivalent to b mod m
    if (mod < 0) return leastPosEquiv(a, -1 * mod)
    //if 0 <= a < m, then a is the least positive integer equivalent to a mod m
    if (a in 0..<mod) return a

    //for negative a, find the least negative integer equivalent to a mod m
    //then add m
    if (a < 0) return -1 * leastPosEquiv(-1 * a, mod) + mod

    //the only case left is that of a,m > 0 and a >= m

    //take the remainder according to the Division algorithm
    val q = a / mod

    /*
     * a = qm + r, with 0 <= r < m
     * r = a - qm is equivalent to a mod m
     * and is the least such non-negative number (since r < m)
     */return a - q * mod
}

fun crt(input: List<Pair<Int, Int>>): Long {
    /*
	 * note that the values in mods must be mutually prime
	 */
    val (constraints: List<Int>, mods: List<Int>) = input.unzip()

    //prodOfMods is the product of the mods
    val prodOfMods: Long = mods.map { it.toLong() }.reduce { acc, l -> acc * l }
    val multInv = LongArray(constraints.size) { i ->
        euclidean(prodOfMods / mods[i], mods[i].toLong()).first
    }

    /*
     * this loop applies the Euclidean algorithm to each pair of M/mods[i] and mods[i]
     * since it is assumed that the various mods[i] are pairwise coprime,
     * the end result of applying the Euclidean algorithm will be
     * gcd(M/mods[i], mods[i]) = 1 = a(M/mods[i]) + b(mods[i]), or that a(M/mods[i]) is
     * equivalent to 1 mod (mods[i]). This a is then the multiplicative
     * inverse of (M/mods[i]) mod mods[i], which is what we are looking to multiply
     * by our constraint constraints[i] as per the Chinese Remainder Theorem
     * euclidean(M/mods[i], mods[i])[0] returns the coefficient a
     * in the equation a(M/mods[i]) + b(mods[i]) = 1
     */
    var x = 0L

    //x = the sum over all given i of (M/mods[i])(constraints[i])(multInv[i])
    for (i in mods.indices) x += prodOfMods / mods[i] * constraints[i] * multInv[i]
    return leastPosEquiv(x, prodOfMods)
}