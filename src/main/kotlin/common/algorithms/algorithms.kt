package common.algorithms


// Returns a sequence of all permutations of the first numbers in (0 until n)
fun permutations(n: Int): Sequence<List<Int>> {
    require(n >= 0)
    // Base case: for n = 0 or 1, there is only 1 permutation, which is the empty list or the singleton list [1]
    if (n == 0) return emptySequence()
    if (n == 1) return sequenceOf(List(1) { 0 })

    // Recursive case: generate the permutations for n - 1, and then insert n at all possible positions in each permutation
    return permutations(n - 1).flatMap { perm: List<Int> ->
        (0..perm.size) // Insert n to every position in the previous permutation
            .map { i: Int ->
                perm.toMutableList().apply { add((perm.size - i), n - 1) }
            }
    }
}