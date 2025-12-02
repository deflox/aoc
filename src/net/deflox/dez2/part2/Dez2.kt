package net.deflox.dez2.part2

fun main() {

//    val input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"
    val input = "9100-11052,895949-1034027,4408053-4520964,530773-628469,4677-6133,2204535-2244247,55-75,77-96,6855-8537,55102372-55256189,282-399,228723-269241,5874512-6044824,288158-371813,719-924,1-13,496-645,8989806846-8989985017,39376-48796,1581-1964,699387-735189,85832568-85919290,6758902779-6759025318,198-254,1357490-1400527,93895907-94024162,21-34,81399-109054,110780-153182,1452135-1601808,422024-470134,374195-402045,58702-79922,1002-1437,742477-817193,879818128-879948512,407-480,168586-222531,116-152,35-54"
    val ranges = input.split(",")
    var sum: Long = 0
    for (range in ranges) {
        val start = range.split("-")[0].toLong()
        val end = range.split("-")[1].toLong()
        for (numericNumber in start..end) {
            val number = numericNumber.toString()
            if (hasAtLeastTwoEqualParts(number)) {
                sum += numericNumber
            }
        }
    }

    println(sum)

}

fun hasAtLeastTwoEqualParts(number: String): Boolean {
    val first = number[0]
    for (i in 1..<number.length) {
        if (number[i] == first && number.length % (i) == 0 && hasEqParts(number.length / i, number)) {
            return true
        }
        if ((i+1) > number.length / 2) { // not possible anymore as we exceed half the word
            return false;
        }
    }
    return false;
}

fun hasEqParts(parts: Int, number: String): Boolean {
    val partLength = number.length / parts
    var splitPart = number.take(partLength)
    for (i in 1..<parts) {
        if (splitPart != number.substring(i*partLength, i*partLength + partLength)) {
            return false
        }
        splitPart = number.substring(i*partLength, i*partLength + partLength)
    }
    return true;
}