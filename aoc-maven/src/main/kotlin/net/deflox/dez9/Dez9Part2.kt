package org.example.net.deflox.dez9

import java.io.File

fun main() {
    val points = mutableListOf<Point>()
    File("input.txt").reader().forEachLine {
        val parse = it.split(",").map { i -> i.toLong() }
        points.add(Point(parse[0], parse[1]))
    }

    val maxY = points.maxBy { it.y }.y
    val maxX = points.maxBy { it.x }.x

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

    groupedByColumn.forEach { it.value.sort() }
    groupedByLine.forEach { it.value.sort() }

    var biggestArea = 0L
    points.forEach { p1 ->
        points.forEach { p2 ->
            val opP1 = Point(p1.x, p2.y)
            val opP2 = Point(p2.x, p1.y)
            if (check(opP1, p1, groupedByLine, groupedByColumn, maxX, maxY) && check(opP2, p2, groupedByLine, groupedByColumn, maxX, maxY)) {
                val newArea = (-1L * (p1.x - p2.x + 1)) * (-1L * (p1.y - p2.y + 1))
                if (newArea > biggestArea) {
                    biggestArea = newArea
                }
            }

        }
    }

    println(biggestArea)

}

fun check(oppositePoint: Point, point: Point, groupedByLine: MutableMap<Long, MutableList<Long>>, groupedByColumn: MutableMap<Long, MutableList<Long>>, maxX: Long, maxY: Long): Boolean {
    val column = groupedByColumn[oppositePoint.x]!!
    val columnCheck = oppositePoint.y >= column[0] && oppositePoint.y <= column[1]
    val row = groupedByLine[oppositePoint.y]!!
    val rowCheck = oppositePoint.x >= row[0] && oppositePoint.x <= row[1]
    if (columnCheck && rowCheck) {
        return true
    }

    // y-check
    var foundYWall = false
    if (oppositePoint.y > point.y) {
        // go upward
        var curr = oppositePoint.y
        while (curr <= maxY) {
            if (groupedByLine.containsKey(curr) && groupedByLine[curr]!![0] <= oppositePoint.x && groupedByLine[curr]!![1] >= oppositePoint.x) {
                foundYWall = true
                break
            }
            curr += 1
        }
    } else {
        // go downward
        var curr = oppositePoint.y
        while (curr >= 0L) {
            if (groupedByLine.containsKey(curr) && groupedByLine[curr]!![0] <= oppositePoint.x && groupedByLine[curr]!![1] >= oppositePoint.x) {
                foundYWall = true
                break
            }
            curr -= 1
        }
    }

    // x-check
    var foundXWall = false
    if (oppositePoint.x > point.x) {
        // go right
        var curr = oppositePoint.x
        while (curr <= maxX) {
            if (groupedByColumn.containsKey(curr) && groupedByColumn[curr]!![0] <= oppositePoint.y && groupedByColumn[curr]!![1] >= oppositePoint.y) {
                foundXWall = true
                break
            }
            curr += 1
        }
    } else {
        // go left
        var curr = oppositePoint.x
        while (curr >= 0L) {
            if (groupedByColumn.containsKey(curr) && groupedByColumn[curr]!![0] <= oppositePoint.y && groupedByColumn[curr]!![1] >= oppositePoint.y) {
                foundXWall = true
                break
            }
            curr -= 1
        }
    }

    return foundXWall && foundYWall
}

class Point(val x: Long, val y: Long)