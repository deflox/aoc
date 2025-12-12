package net.deflox.dez09.part02.v3

import java.io.File
import kotlin.math.abs

fun main() {
    val points = mutableListOf<Point>()
    val horizontalPoints = mutableMapOf<Long, MutableList<Long>>()
    val verticalPoints = mutableMapOf<Long, MutableList<Long>>()
    File("input.txt").reader().forEachLine {
        val coordinates = it.split(",").map { i -> i.toLong() }
        val point = Point(coordinates[0], coordinates[1])
        points.add(point)
        horizontalPoints.getOrPut(point.y, { mutableListOf() }).add(point.x)
        verticalPoints.getOrPut(point.x, { mutableListOf() }).add(point.y)
    }

    // 3041911911
    val seen = mutableSetOf<PointPair>()
    var biggestArea = 0L
    var biggestPointPair: PointPair? = null
    points.forEachIndexed { index, p1 ->
        println("Processing: $index: $p1")
        points.forEach { p2 ->
            if (p1 != p2 && !seen.contains(PointPair(p1, p2))) {
                if (insideShape(p1, p2, horizontalPoints, verticalPoints)) {
                    val newArea = (abs(p1.x - p2.x) + 1) * (abs(p1.y - p2.y) + 1)
                    if (newArea > biggestArea) {
                        biggestArea = newArea
                        biggestPointPair = PointPair(p1, p2)
                    }
                }
                seen.add(PointPair(p2, p1))
            }
        }
    }

    println(biggestArea)
    println(biggestPointPair)

}

fun insideShape(p1: Point, p2: Point, horizontalPoints: MutableMap<Long, MutableList<Long>>, verticalPoints: MutableMap<Long, MutableList<Long>>): Boolean {

    if (p1.y == p2.y) return true // if exactly on same edge it is within shape
    else if (p1.x == p2.x) return true // if exactly on same edge it is within shape

    val x = arrayOf(p1.x, p2.x)
    val yRange = if (p1.y > p2.y) p2.y..p1.y else p1.y..p2.y
    for (y in yRange) {
        if (pointHitsHorizontalWall(Point(x[0], y), horizontalPoints) && outsideHorizontalWall(Point(x[0], y), yRange, horizontalPoints, verticalPoints)) return false
        if (pointHitsHorizontalWall(Point(x[1], y), horizontalPoints) && outsideHorizontalWall(Point(x[1], y), yRange, horizontalPoints, verticalPoints)) return false
    }

    val y = arrayOf(p1.y, p2.y)
    val xRange = if (p1.x > p2.x) p2.x..p1.x else p1.x..p2.x
    for (x in xRange) {
        if (pointHitsVerticalWall(Point(x, y[0]), verticalPoints) && outsideVerticalWall(Point(x, y[0]), xRange, horizontalPoints, verticalPoints)) return false
        if (pointHitsVerticalWall(Point(x, y[1]), verticalPoints) && outsideVerticalWall(Point(x, y[1]), xRange, horizontalPoints, verticalPoints)) return false
    }

    return true

}

fun outsideHorizontalWall(p: Point, yRange: LongRange, horizontalPoints: MutableMap<Long, MutableList<Long>>, verticalPoints: MutableMap<Long, MutableList<Long>>): Boolean {
    val outsideDirection = if (horizontalPoints[p.y]!![0] < horizontalPoints[p.y]!![1]) -1 else 1
    val range = if (horizontalPoints[p.y]!![0] < horizontalPoints[p.y]!![1]) horizontalPoints[p.y]!![0]..horizontalPoints[p.y]!![1] else horizontalPoints[p.y]!![1]..horizontalPoints[p.y]!![0]
    val failingRange = calculateFailingRange(range.min(), range.max(), outsideDirection, verticalPoints[range.min()]!!.first { it != p.y } > p.y, verticalPoints[range.max()]!!.first { it != p.y } > p.y)

    // wenn aussen nach oben ist -> gibt es noch spielraum nach oben und ist x im "fail" bereich? -> fail
    // wenn aussen nach unten ist -> gibt es noch spielraum nach unten und ist x im "fail" bereich? ? -> fail

    if (outsideDirection == 1 && p.y < yRange.max() && failingRange.contains(p.x)) return true
    else if (outsideDirection == -1 && p.y > yRange.min() && failingRange.contains(p.x)) return true
    else return false
}

fun outsideVerticalWall(p: Point, xRange: LongRange, horizontalPoints: MutableMap<Long, MutableList<Long>>, verticalPoints: MutableMap<Long, MutableList<Long>>): Boolean {
    val outsideDirection = if (verticalPoints[p.x]!![0] < verticalPoints[p.x]!![1]) 1 else -1
    val range = if (verticalPoints[p.x]!![0] < verticalPoints[p.x]!![1]) verticalPoints[p.x]!![0]..verticalPoints[p.x]!![1] else verticalPoints[p.x]!![1]..verticalPoints[p.x]!![0]
    val failingRange = calculateFailingRange(range.min(), range.max(), outsideDirection, horizontalPoints[range.min()]!!.first { it != p.x } > p.x, horizontalPoints[range.max()]!!.first{ it != p.x } > p.x)

    if (outsideDirection == 1 && p.x < xRange.max() && failingRange.contains(p.y)) return true
    else if (outsideDirection == -1 && p.x > xRange.min() && failingRange.contains(p.y)) return true
    else return false
}

fun calculateFailingRange(from: Long, to: Long, direction: Int, leftUpwards: Boolean, rightUpwards: Boolean): LongRange {
    if (direction == 1 && leftUpwards && rightUpwards) return from+1..to-1
    if (direction == 1 && leftUpwards && !rightUpwards) return from+1..to
    if (direction == 1 && !leftUpwards && rightUpwards) return from..to-1
    if (direction == -1 && !leftUpwards && !rightUpwards) return from+1..to-1
    if (direction == -1 && leftUpwards && !rightUpwards) return from..to-1
    if (direction == -1 && !leftUpwards && rightUpwards) return from+1..to
    return from..to
}

fun pointHitsHorizontalWall(p: Point, horizontalPoints: MutableMap<Long, MutableList<Long>>): Boolean {
    if (horizontalPoints.containsKey(p.y)) {
        val range = horizontalPoints[p.y]!!.map { it }.sorted()
        return range[0] <= p.x && p.x <= range[1]
    }
    return false
}

fun pointHitsVerticalWall(p: Point, verticalPoints: MutableMap<Long, MutableList<Long>>): Boolean {
    if (verticalPoints.containsKey(p.x)) {
        val range = verticalPoints[p.x]!!.map { it }.sorted()
        return range[0] <= p.y && p.y <= range[1]
    }
    return false
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

    override fun toString(): String {
        return "PointPair(p1=$p1, p2=$p2)"
    }


}