package net.deflox.dez09.blub

import java.awt.Polygon
import java.io.File

fun main() {
    val points = mutableSetOf<Point>()
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

    val seen = mutableSetOf<PointPair>()
    var biggestArea = 0L
    points.forEachIndexed { index, p1 ->
        println("Processing: $index: $p1")
        points.forEach { p2 ->
            if (p1 != p2 && !seen.contains(PointPair(p1, p2))) {
                if (insideShape(p1, p2, polygon, points, lines, columns)) {
                    val newArea = (-1L * (p1.x - p2.x + 1)) * (-1L * (p1.y - p2.y + 1))
                    if (newArea > biggestArea) {
                        biggestArea = newArea
                    }
                }
                seen.add(PointPair(p2, p1))
            }
        }
    }

    println(biggestArea)

}

fun insideShape(p1: Point, p2: Point, polygon: Polygon, points: MutableSet<Point>, lines: MutableMap<Long, Range>, columns: MutableMap<Long, Range>): Boolean {

    val x = arrayOf(p1.x, p2.x)
    val yRange = if (p1.y > p2.y) p2.y..p1.y else p1.y..p2.y
    for (y in yRange) {
        if (!pointInsideShape(Point(x[0], y), polygon, points, lines, columns)) return false
        if (!pointInsideShape(Point(x[1], y), polygon, points, lines, columns)) return false
    }

    val y = arrayOf(p1.y, p2.y)
    val xRange = if (p1.x > p2.x) p2.x..p1.x else p1.x..p2.x
    for (x in xRange) {
        if (!pointInsideShape(Point(x, y[0]), polygon, points, lines, columns)) return false
        if (!pointInsideShape(Point(x, y[1]), polygon, points, lines, columns)) return false
    }

    return true

}

fun pointInsideShape(p: Point, polygon: Polygon, points: MutableSet<Point>, lines: MutableMap<Long, Range>, columns: MutableMap<Long, Range>): Boolean {
    if (points.contains(p)) return true
    else if (lines.containsKey(p.y) && lines[p.y]!!.from <= p.x && p.x <= lines[p.y]!!.to) return true
    else if (columns.containsKey(p.x) && columns[p.x]!!.from <= p.y && p.y <= columns[p.x]!!.to) return true
    return polygon.contains(p.x.toInt(), p.y.toInt())
}

class Point(val x: Long, val y: Long) {
    override fun toString(): String {
        return "($x,$y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}

class Range(var from: Long, var to: Long) {
    override fun toString(): String {
        return "($from -> $to)"
    }
}

class PointPair(val p1: Point, val p2: Point) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointPair

        if (p1 != other.p1) return false
        if (p2 != other.p2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = p1.hashCode()
        result = 31 * result + p2.hashCode()
        return result
    }
}