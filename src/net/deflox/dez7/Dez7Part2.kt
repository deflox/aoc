package net.deflox.dez7

import java.io.File

fun main() {

    val lines = mutableListOf<MutableList<Char>>()

    File("input.txt").reader().forEachLine {
        lines.add(it.toCharArray().toMutableList())
    }

    val count = mutableListOf<Long>()
    lines[2].forEach { char ->
        if (char == '^') count.add(1)
        else count.add(0)
    }

    for (i in 2..lines.size - 2 step 2) {
        lines[i].forEachIndexed { charIndex, char ->
            if (char == '^') {
                count[charIndex - 1] = if (count[charIndex - 1] == 0L) count[charIndex] else count[charIndex - 1] + count[charIndex]
                count[charIndex + 1] = if (count[charIndex + 1] == 0L) count[charIndex] else count[charIndex + 1] + count[charIndex]
                count[charIndex] = 0
            }
        }
    }

    println(count.reduce { c, n -> c + n })

}