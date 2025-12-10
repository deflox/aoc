package net.deflox.dez09

import java.awt.Polygon
import java.io.File

fun main() {
    val points = mutableListOf<Point>()

    val polygon = Polygon()
    File("input.txt").reader().forEachLine {
        val parse = it.split(",").map { i -> i.toLong() }
        points.add(Point(parse[0], parse[1]))
        polygon.addPoint(parse[0].toInt(), parse[1].toInt())
    }

    val lines = mutableMapOf<Long, Range>()
    points.sortedWith(compareBy(Point::y, Point::x)).toMutableList().forEach { point ->
        if (!lines.containsKey(point.y)) {
            lines[point.y] = Range(point.x, 0)
        }
        lines[point.y] = Range(lines[point.y]!!.from, point.x)
    }

    val columns = mutableMapOf<Long, Range>()
    points.sortedWith(compareBy(Point::x, Point::y)).toMutableList().forEach { point ->
        if (!columns.containsKey(point.x)) {
            columns[point.x] = Range(point.y, 0)
        }
        columns[point.x] = Range(columns[point.x]!!.from, point.y)
    }

    var biggestArea = 0L
    points.forEach { p1 ->
        points.forEach { p2 ->
            if (insideShape(p1, p2, polygon)) {
                val newArea = (-1L * (p1.x - p2.x + 1)) * (-1L * (p1.y - p2.y + 1))
                if (newArea > biggestArea) {
                    biggestArea = newArea
                }
            }
        }
    }

    println(biggestArea)

}

fun insideShape(p1: Point, p2: Point, polygon: Polygon): Boolean {

    val x = arrayOf(p1.x, p2.x)
    val yRange = if (p1.y > p2.y) p2.y..p1.y else p1.y..p2.y
    for (y in yRange) {
        if (!polygon.contains(x[0].toInt(), y.toInt())) return false
        if (!polygon.contains(x[1].toInt(), y.toInt())) return false
    }

    val y = arrayOf(p1.y, p2.y)
    val xRange = if (p1.x > p2.x) p2.x..p1.x else p1.x..p2.x
    for (x in xRange) {
        if (!polygon.contains(x.toInt(), y[0].toInt())) return false
        if (!polygon.contains(x.toInt(), y[1].toInt())) return false
    }

    return true

}

class Point(val x: Long, val y: Long) {
    override fun toString(): String {
        return "($x,$y)"
    }


}
class Range(var from: Long, var to: Long, val finished: Boolean = false) {
    override fun toString(): String {
        return "($from -> $to)"
    }

    fun distance(): Long {
        return to - from
    }
}

// wenn es zusammengeht dann muss jetzt noch mit der alten ranges geprintet werden, sonst wäre auf dieser zeile nur die zusammengeshrinkte version
// true = use new ranges right away
fun updateRanges(curr: MutableList<Range>, new: Range): Boolean {
    if (curr[0].from == new.from && curr[curr.size - 1].to == new.to) {
        return true // do nothing
    }

    else if (curr[0].from == new.to) { // extend left
        curr[0].from = new.from
        return true
    }

    else if (curr[curr.size - 1].to == new.from) { // extend right
        curr[curr.size - 1].to = new.to
        return true
    }

    else if (curr[0].from == new.from) { // inwards right
        if (new.to >= curr[0].to) {
            while (new.to >= curr[0].to) {
                curr.removeAt(0)
            }
        }

        curr[0].from = new.to

        return false

    }

    else if (curr[curr.size - 1].to == new.to) { // inwards left
        if (new.from <= curr[curr.size - 1].from) {
            while (new.from <= curr[curr.size - 1].from) {
                curr.removeAt(curr.size - 1)
            }
        }

        curr[curr.size - 1].to = new.from

        return false

    }

    else if (new.to < curr[0].from) {
        curr.add(0, Range(new.from, new.to))
        return true

    }

    else if (new.from > curr[curr.size - 1].to) {
        curr.add(Range(new.from, new.to))
        return true

    }

    else {
        throw IllegalArgumentException()
    }
}

//    val yMin = lines.keys.min()
//    val yMax = lines.keys.max()
//    var currentRanges = mutableListOf(lines[yMin]!!)
//    val allLines = mutableMapOf<Long, MutableList<Range>>()
//    for (i in yMin..yMax) {
//        if (lines.containsKey(i)) {
//            val oldRanges = currentRanges.map { Range(it.from,it.to) }.toMutableList()
//            val printRightAway = updateRanges(currentRanges, lines[i]!!)
//            allLines[i] = if (printRightAway) currentRanges else oldRanges
//        } else {
//            allLines[i] = currentRanges
//        }
//    }
//
//    val xMin = columns.keys.min()
//    val xMax = columns.keys.max()

//    val groupedByColumn = mutableMapOf<Long, MutableList<Long>>()
//    if (!groupedByColumn.containsKey(point.x)) {
//        groupedByColumn[point.x] = mutableListOf()
//    }
//
//    val maxY = points.maxBy { it.y }.y
//    val minY = points.minBy { it.y }.y
//    val maxX = points.maxBy { it.x }.x
//    val minX = points.minBy { it.x }.x
//
//    var currRange = Range(minX, maxX)
//    val rangesX = mutableMapOf<Long, Range>()
//    for (i in minY+1..maxY) {
//        if (lines.containsKey(i)) {
//            currRange = newRange(currRange, Range(lines[i]!![0], lines[i]!![1]))
//            rangesX[i] = currRange
//        } else {
//            rangesX[i] = currRange
//        }
//    }
//
//    currRange = Range(minY, maxY)
//    val rangesY = mutableMapOf<Long, Range>()
//    for (i in minX+1..maxX) {
//        if (groupedByColumn.containsKey(i)) {
//            currRange = newRange(currRange, Range(groupedByColumn[i]!![0], groupedByColumn[i]!![1]))
//            rangesY[i] = currRange
//        } else {
//            rangesY[i] = currRange
//        }
//    }
//
//    rangesY.size