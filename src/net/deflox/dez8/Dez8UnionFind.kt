package net.deflox.dez8

import java.io.File
import kotlin.math.sqrt

fun main() {

    val points = mutableListOf<Point>()

    File("input.txt").reader().forEachLine {
        val coordinates = it.split(",").map { i -> i.toLong() }
        points.add(Point(coordinates[0], coordinates[1], coordinates[2], null))
    }

    val seenOppositePairs = mutableSetOf<Pair>()
    var distances = mutableListOf<Distance>()

    points.forEach { p1 ->
        points.forEach { p2 ->
            if (p1 != p2 && !seenOppositePairs.contains(Pair(p1, p2))) {
                val distance: Double = sqrt(((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z)).toDouble())
                distances.add(Distance(p1, p2, distance))
                seenOppositePairs.add(Pair(p2, p1))
            }
        }
    }


    val rootSizes = mutableMapOf<Point, Long>()
    distances.forEach { distance ->
        rootSizes[distance.p1] = 1
        rootSizes[distance.p2] = 1
    }
    distances = distances.sortedBy { it.distance }.take(10).toMutableList()
    distances.forEach { distance ->
        val principal1 = find(distance.p1)
        val principal2 = find(distance.p2)
        if (principal1 != principal2) {
            principal1.parent = principal2
            rootSizes[principal2] = rootSizes[principal1]!! + rootSizes[principal2]!!
            rootSizes.remove(principal1)
        }
    }

    println(rootSizes.values.sorted().reversed().take(3).reduce { e, n -> e*n })

}

fun find(p: Point): Point {
    return if (p.parent == null) p else find(p.parent!!)
}

class Pair(val p1: Point, val p2: Point) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pair

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

class Point(val x: Long, val y: Long, val z: Long, var parent: Point?, var rank: Int = 1) {
    override fun toString(): String {
        return "Point($x,$y,$z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

}

class Distance(val p1: Point, val p2: Point, val distance: Double) {
    override fun toString(): String {
        return "Distance(p1=$p1, p2=$p2, distance=$distance)"
    }
}