package org.example.net.deflox.dez5.part1

import java.io.File

fun main() {
    var startCheck = false
    val fresh = mutableListOf<Array<Long>>()
    var freshItems = 0
    File("input.txt").reader().forEachLine {
        if (it == "") {
            startCheck = true
        } else if (!startCheck) {
            val start = it.split("-")[0].toLong()
            val end = it.split("-")[1].toLong()
            fresh.add(arrayOf(start, end))
        } else if (startCheck) {
            freshItems = if (isFresh(fresh, it.toLong())) freshItems + 1 else freshItems
        }
    }

    println(freshItems)

}

fun isFresh(ranges: List<Array<Long>>, check: Long): Boolean {
    ranges.forEach { range ->
        if (check >= range.first() && check <= range.last()) {
            return true
        }
    }

    return false
}