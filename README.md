# AdventOfCode2021

- My first steps in Kotlin by enjoying the puzzles of this year, Advent of Code 2021.

- Who needs readability if the answer is short, right? Right?

| Day | Solved | Styled |
|-----|--------|--------|
| [1](https://github.com/jkrude/AdventOfCode2021/blob/master/src/main/kotlin/Day1.kt)   | :star: | :heavy_check_mark: |
| 2                                                                                     | :star: |                    |
| [3](https://github.com/jkrude/AdventOfCode2021/blob/master/src/main/kotlin/Day3.kt)   | :star: | :heavy_check_mark: |
| 4                                                                                     | :star: |                    |
| 5                                                                                     | :star: |                    |
| [6](https://github.com/jkrude/AdventOfCode2021/blob/master/src/main/kotlin/Day6.kt)   | :star: | :heavy_check_mark: |
| [7](https://github.com/jkrude/AdventOfCode2021/blob/master/src/main/kotlin/Day7.kt)   | :star: | :heavy_check_mark: |
| [8](https://github.com/jkrude/AdventOfCode2021/blob/master/src/main/kotlin/Day8.kt)   | :star: | :heavy_check_mark: |
| [9](https://github.com/jkrude/AdventOfCode2021/blob/master/src/main/kotlin/Day9.kt)   | :star: | :heavy_check_mark: |

# Interesting findings

## Day 7

- Let _X_ be a subset of the natural numbers and _i_ some natural number
- The cost function *c* maps X, i to sum { abs(x - i) | x is in X}

##### → The optimum for a given X w.r.t. c is the median of X

#### Why?

let *m* be the median of X

- If you use some number j smaller than m:
- All numbers smaller than j will have a reduced cost of m-j,
- however, there are at least as many values which costs you increase by m-j with the additional new cost for m
- if you have an equal amount of numbers in X both ceil(|X|/2) and floor(|X|/2) are optima

Example:

- X = [0, 5,6] -> 5 is the optima
    - if we move it to the left 4 - 0 gets smaller however 6-4 increases by the same amount and the new cost 5-4 is
      added
- X = [0,1] -> 0, 1, 0.5 are optima
    - It doesnt make a difference if you move 0 by one or 1 by one or both (2*) by 0.5
