package net.deflox.dez6.part1

import java.io.File

fun main() {
    val lines = mutableListOf<String>()
    File("input.txt").reader().forEachLine { lines.add(it) }

    var total: Long = 0
    val longestLine = lines.subList(0, lines.size - 2).map { it.length }.max()

    var operator = "";
    var collectedNumbers = mutableListOf<Long>()
    for (pos in 0..longestLine) {
        if (problemDelimiter(lines, pos)) {
            // calculate problem and add to total and reset everything
            var result: Long = if (operator == "*") 1 else 0
            collectedNumbers.forEach { no ->
                result = if (operator == "*") result * no else result + no
            }

            total += result
            operator = ""
            collectedNumbers.clear()

        } else {
            var number = "";
            for (lineNumber in 0..lines.size - 2) {
                number = if (pos < lines[lineNumber].length && lines[lineNumber][pos] != ' ') number + lines[lineNumber][pos].toString() else number + ""
            }
            collectedNumbers.add(number.toLong())
            if (pos < lines[lines.size -1].length && lines[lines.size - 1][pos] != ' ') {
                operator = lines[lines.size - 1][pos].toString()
            }
        }

    }

    println(total)

}

fun problemDelimiter(lines: List<String>, pos: Int): Boolean {
    var elements = ""
    lines.forEach { line ->
        elements += if (pos < line.length) line[pos].toString().trim() else ""
    }
    return elements == ""
}