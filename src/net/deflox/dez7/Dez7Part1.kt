package net.deflox.dez7

import java.io.File

fun main() {

    val lines = mutableListOf<MutableList<Char>>()

    File("input.txt").reader().forEachLine {
        lines.add(it.toCharArray().toMutableList())
    }

    val foundPositions = mutableMapOf<Int, MutableSet<Int>>()
    foundPositions.getOrPut(2) { mutableSetOf() }.add(findFirstSplit(lines[2]))

    for (i in 2..lines.size - 2 step 2) {
        if (i + 2 != lines.size && foundPositions.contains(i)) {
            val positionsToCheck = foundPositions[i]?.map { p -> listOf(p - 1, p + 1) }?.flatten()?.toMutableSet()!!
            for (y in i + 2..lines.size - 2 step 2) {
                val found = positionsToCheck.filter { pos -> lines[y][pos] == '^'}.toSet()
                foundPositions.getOrPut(y) { mutableSetOf() }.addAll(found)
                positionsToCheck.removeAll(found)
                if (positionsToCheck.isEmpty()) break

            }

        }

    }

    println(foundPositions.map { entry -> entry.value.size }.reduce { c, n -> c + n })

}

fun findFirstSplit(line: List<Char>): Int {
    line.forEachIndexed { index, ch -> if (ch == '^') return index }
    throw IllegalStateException()
}