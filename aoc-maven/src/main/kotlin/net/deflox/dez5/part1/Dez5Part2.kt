package org.example.net.deflox.dez5.part1

import java.io.File

fun main() {
    val ranges = mutableListOf<Array<Long>>()
    File("input.txt").reader().forEachLine {
        val start = it.split("-")[0].toLong()
        val end = it.split("-")[1].toLong()
        ranges.add(arrayOf(start, end))
    }

    ranges.forEachIndexed { index, rangeToMerge ->

        val hasBeenMerged = merge(ranges, rangeToMerge, index)
        if (hasBeenMerged) {
            rangeToMerge[0] = -1
            rangeToMerge[1] = -1
        }

    }

    var sum: Long = 0
    ranges.forEach { range -> sum += if (range[0] != -1L && range[1] != -1L) range[1] - range[0] + 1 else 0 }
    println(sum)

}

fun merge(ranges: MutableList<Array<Long>>, rangeToMerge: Array<Long>, currentIndex: Int): Boolean {

    ranges.forEachIndexed { index, range ->

        if (currentIndex != index && range[0] != -1L && range[1] != -1L) {
            if (rangeToMerge[0] >= range[0] && rangeToMerge[1] <= range[1]) {
                // range to merge is within a range, therefor we just do nothing
                return true
            } else if (rangeToMerge[0] < range[0] && rangeToMerge[1] >= range[0] && rangeToMerge[1] <= range[1]) {
                // end of range to merge is within this range so new start of this range is the start of the range to merge
                range[0] = rangeToMerge[0]
                return true
            } else if (rangeToMerge[0] >= range[0] && rangeToMerge[0] <= range[1] && rangeToMerge[1] > range[1]) {
                // start of range to merge is within this range so new end is the end of the range to merge
                range[1] = rangeToMerge[1]
                return true
            } else if (rangeToMerge[0] < range[0] && rangeToMerge[1] > range[1]) {
                // range to merge surrounds the current range, therefore we can completely replace it with range to merge
                range[0] = rangeToMerge[0]
                range[1] = rangeToMerge[1]
                return true
            }
        }

    }

    return false

}