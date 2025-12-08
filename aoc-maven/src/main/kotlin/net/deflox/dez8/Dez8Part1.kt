package org.example.net.deflox.dez8

import java.io.File
import kotlin.math.sqrt

fun main() {

    val points = mutableListOf<Point>()

    File("input.txt").reader().forEachLine {
        val coordinates = it.split(",").map { i -> i.toLong() }
        points.add(Point(coordinates[0], coordinates[1], coordinates[2]))
    }

    var distances = mutableListOf<Distance>()

    points.forEach { p1 ->
        points.forEach { p2 ->
            if (p1 != p2) {
                val distance: Double = sqrt(((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z)).toDouble())
                distances.add(Distance(p1, p2, distance))
            }
        }

    }

    distances = distances.sortedBy { it.distance }.toMutableList()

    val circuits = mutableListOf<MutableSet<Point>>()
    points.forEach {
        circuits.add(mutableSetOf(it))
    }

    distances.forEach { distance ->
        val fromIndex = findIndex(circuits, distance.from)
        val toIndex = findIndex(circuits, distance.to)
        if (fromIndex != toIndex) {
            circuits[fromIndex].addAll(circuits[toIndex])
            circuits.removeAt(toIndex)
        }

        if (circuits.size == 1) {
            println(distance.from.x * distance.to.x)
        }

    }


    val sortedCircuits = circuits.toList().sortedBy { circuit -> circuit.size }.reversed()

    println(sortedCircuits[0].size * sortedCircuits[1].size * sortedCircuits[2].size)

}

fun findIndex(circuits: MutableList<MutableSet<Point>>, point: Point): Int {
    for (i in 0..<circuits.size) {
        if (circuits[i].contains(point)) {
            return i
        }
    }

    throw IllegalArgumentException("Not found")
}