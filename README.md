### Welcome to my Advent of Code Repository 🎄
I love solving quirky [puzzles](https://adventofcode.com/) with my favorite [programming language](https://kotlinlang.org/) and publishing all my solutions here.
I have participated every year since 2020 with varying stamina 🏃‍♂️.
The solutions for each year are placed in separate sub-packages (e.g. `y2025`).
There is also some common code for the setup script (`common.Setup`) and handy algorithms, as well as some useful extensions.
#### Solutions
You can follow my early steps with Kotlin in the year 2020 or my later attempts at shorter or more readable code.
| Year | Collected ⭐ | Max ⭐|
|------|-----------------|----------|
|[2025](src/main/kotlin/y2025) | 8 | 24 |
|[2024](src/main/kotlin/y2024) | 42 | 50 |
|[2023](src/main/kotlin/y2023) | 48 | 50 |
|[2022](src/main/kotlin/y2022) | 48 | 50 |
|[2021](src/main/kotlin/y2021) | 36 | 50 |
|[2020](src/main/kotlin/y2020) | 39 | 50 |

#### Extensions and Algorithms
You can find utility functions for 2D points (`Point2D`) grids (`List2D`) or search algorithms (`Search`, `Dijkstra` etc.).
Using the search builder, it is easy to formulate breadth-first search problems:
```kotlin
Search.startingFrom(ij) // a two-dimensional index Idx2d
                .neighbors {
                    it.neighbours() // extension function of Idx2D
                        .filter { n -> grid.containsIndex(n) }
                        .filter { n -> grid[n] == grid[ij] }
                }
                .onEachVisit(newRegion::add)
                .executeBfs() // or simply swap with .executeDfs()
```
#### Concise SAT Solving with Z3
For particularly lazy solutions or tricky SAT problems, there is a Kotlin wrapper around the [Z3 solver](https://github.com/Z3Prover/z3).
It offers extension and infix functions for a lot of operations, enabling a cleaner syntax.
Using infix functions, operator overloading, and extension functions, we can get close to the conciseness of the python API.
For example, you can write something like this:
```kotlin
with(Kontext()) {
    val solver = mkSolver()
    val x = mkIntConst("x")
    val y = mkIntConst("y")
    solver.add((x gt 2) and ((x * y) eq 12))
    solver.check()
    print(solver.model)
}
```
