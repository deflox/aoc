package net.deflox.dez3.part2

import java.io.File

fun main() {

    var sum: Long = 0

    File("input.txt").reader().forEachLine {
        var numbers = it.toCharArray().map { c -> c.toString().toInt() }
        var result = ""
        for (digit in 12 downTo 1) {
            val pos = searchLeftMostHighestNumber(numbers, numbers.size - digit)
            result += numbers[pos]
            numbers = numbers.subList(pos + 1, numbers.size)
        }

        println(result)

        sum += result.toLong()

    }

    println(sum)

}

fun searchLeftMostHighestNumber(numbers: List<Int>, until: Int): Int {
    var highestPos = until
    for (i in until downTo 0) {
        highestPos = if (numbers[i] >= numbers[highestPos]) i else highestPos
    }
    return highestPos
}