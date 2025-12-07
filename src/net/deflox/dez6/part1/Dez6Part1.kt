package net.deflox.dez6.part1

import java.io.File
import java.util.regex.Pattern

fun main() {

    val lines = mutableListOf<List<Long>>()
    val operators = mutableListOf<String>()

    var line = 0
    File("input.txt").reader().forEachLine {
        if (line < 3) {
            val numbers = it.trim().split(Pattern.compile("\\s+")).map { n -> n.toLong() }
            lines.add(numbers)
        } else {
            operators.addAll(it.split(Pattern.compile("\\s+")))
        }

        line++
    }

    var total:Long = 0
    operators.forEachIndexed { index, operator ->
        var result: Long = if (operator == "*") 1 else 0
        lines.forEach { line ->
            result = if (operator == "*") result * line[index] else result + line[index]
        }
        total += result
    }

    println(total)

}