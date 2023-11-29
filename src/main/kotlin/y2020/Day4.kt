package y2020

import common.readFileText


object Day4 {


    /**
     * byr (Birth Year) - four digits; at least 1920 and at most 2002.
     * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
     * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
     * hgt (Height) - a number followed by either cm or in:
     * If cm, the number must be at least 150 and at most 193.
     * If in, the number must be at least 59 and at most 76.
     * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
     * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
     * pid (Passport ID) - a nine-digit number, including leading zeroes.
     */
    private fun parsePassports(text: String): List<Map<String, String>> =
        text
            .split("\n\n")
            .map { passportString: String ->
                passportString.split(" ", "\n")
                    .map { it.split(":") }
                    .associate { it.first() to it.last() }

            }

    private fun validHeight(value: String): Boolean {
        return if (value.endsWith("cm")) {
            value.removeSuffix("cm").toInt() in 150..193
        } else if (value.endsWith("in")) {
            value.removeSuffix("in").toInt() in 59..76
        } else {
            return false
        }
    }

    private fun validatePassportValues(passport: Map<String, String>): Boolean {
        val byr = passport["byr"] ?: return false
        val iyr = passport["iyr"] ?: return false
        val eyr = passport["eyr"] ?: return false
        val hgt = passport["hgt"] ?: return false
        val hcl = passport["hcl"] ?: return false
        val ecl = passport["ecl"] ?: return false
        val pid = passport["pid"] ?: return false
        return (byr.length == 4 && byr.toIntOrNull() in 1920..2002)
                && (iyr.length == 4 && iyr.toIntOrNull() in 2010..2020)
                && (eyr.length == 4 && eyr.toIntOrNull() in 2020..2030)
                && hcl.matches(Regex("#[0-9a-fA-F]{6}"))
                && validHeight(hgt)
                && ecl in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                && pid.length == 9 && pid.toIntOrNull() != null && pid.toInt() > 0
    }

    fun partOne(text: String): Int {
        val passports = parsePassports(text)
        val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        return passports.count { passport ->
            keys.all { passport.containsKey(it) }
        }
    }

    fun partTwo(text: String): Int {
        val passports = parsePassports(text)
        return passports.count {
            validatePassportValues(it)
        }
    }
}

fun main() {
    println(Day4.partOne(readFileText(4, 2020)))
    println(Day4.partTwo(readFileText(4, 2020)))
}