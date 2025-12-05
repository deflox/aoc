package org.example.net.deflox.dez4.part1

import java.io.File

fun main() {

    val input = mutableListOf<MutableList<String>>()

    File("input.txt").reader().forEachLine {
        input.add(it.toCharArray().map { c -> c.toString() }.toMutableList())
    }

    var totalReplacements = 0

    do {
        val replacements = mutableListOf<Array<Int>>()

        input.forEachIndexed { lineNumber, line ->
            line.forEachIndexed { columnNumber, item ->
                if (item == "@") {
                    var total = 0
                    total = if (lineNumber != 0 && input[lineNumber - 1][columnNumber] == "@") total + 1 else total // north
                    total = if (lineNumber != 0 && columnNumber != line.size - 1 && input[lineNumber - 1][columnNumber + 1] == "@") total + 1 else total // north-east
                    total = if (columnNumber != line.size - 1 && input[lineNumber][columnNumber + 1] == "@") total + 1 else total // east
                    total =
                        if (columnNumber != line.size - 1 && lineNumber != input.size - 1 && input[lineNumber + 1][columnNumber + 1] == "@") total + 1 else total // south-east
                    total = if (lineNumber != line.size - 1 && input[lineNumber + 1][columnNumber] == "@") total + 1 else total // south
                    total = if (lineNumber != line.size - 1 && columnNumber != 0 && input[lineNumber + 1][columnNumber - 1] == "@") total + 1 else total // south-west
                    total = if (columnNumber != 0 && input[lineNumber][columnNumber - 1] == "@") total + 1 else total // west
                    total = if (lineNumber != 0 && columnNumber != 0 && input[lineNumber - 1][columnNumber - 1] == "@") total + 1 else total // north-west
                    if (total < 4) {
                        replacements.add(arrayOf(lineNumber, columnNumber))
                    }
                }
            }
        }

        totalReplacements += replacements.size
        replacements.forEach { input[it[0]][it[1]] = "x" }

        input.forEach { println(it.joinToString("")) }
        println()

    } while (replacements.isNotEmpty())

    println(totalReplacements)

}