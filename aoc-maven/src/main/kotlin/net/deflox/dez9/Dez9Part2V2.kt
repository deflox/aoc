package org.example.net.deflox.dez9

import java.io.File

fun main() {
    var points = mutableListOf<Point>()
    File("input.txt").reader().forEachLine {
        val parse = it.split(",").map { i -> i.toLong() }
        points.add(Point(parse[0], parse[1]))
    }

    points = points.sortedWith(compareBy(Point::y, Point::x)).toMutableList()
    val groupedByLine = mutableMapOf<Long, MutableList<Long>>()
    val groupedByColumn = mutableMapOf<Long, MutableList<Long>>()
    points.forEach { point ->
        if (!groupedByLine.containsKey(point.y)) {
            groupedByLine[point.y] = mutableListOf()
        }
        if (!groupedByColumn.containsKey(point.x)) {
            groupedByColumn[point.x] = mutableListOf()
        }
        groupedByLine[point.y]!!.add(point.x)
        groupedByColumn[point.x]!!.add(point.y)
    }

    val maxY = points.maxBy { it.y }.y
    val minY = points.minBy { it.y }.y
    val maxX = points.maxBy { it.x }.x
    val minX = points.minBy { it.x }.x

    var currRange = Range(minX, maxX)
    val rangesX = mutableMapOf<Long, Range>()
    for (i in minY+1..maxY) {
        if (groupedByLine.containsKey(i)) {
            currRange = newRange(currRange.from, currRange.to, groupedByLine.get(i)!![0], groupedByLine.get(i)!![1])
            rangesX[i] = currRange
        } else {
            rangesX[i] = currRange
        }
    }

    currRange = Range(minY, maxY)
    val rangesY = mutableMapOf<Long, Range>()
    for (i in minX+1..maxX) {
        if (groupedByColumn.containsKey(i)) {
            currRange = newRange(currRange.from, currRange.to, groupedByColumn.get(i)!![0], groupedByColumn.get(i)!![1])
            rangesY[i] = currRange
        } else {
            rangesY[i] = currRange
        }
    }

    rangesY.size

}

class Range(val from: Long, val to: Long, val finished: Boolean = false)

fun newRange(curr: Range, new: Range): Range {
    if (curr.from == new.from) {
        // ------------
        //      -------
        return Range(new.to, curr.to)
    } else if (curr.from == new.to) {
        //             ------------
        //  ------------
        return Range(new.from, curr.to)
    } else if (curr.to == new.from) {
        // ------------
        //             -------------
        return Range(curr.from, new.to)
    } else if (curr.to == new.to) {
        // ------------
        //     --------
        return Range(new.from, new.from)
    } else {
        throw IllegalArgumentException()
    }
}