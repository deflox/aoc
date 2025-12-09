package org.example.net.deflox.dez9

import java.io.File
import org.example.net.deflox.dez8.Point

fun main() {
    val coordinates = mutableListOf<Array<Long>>()
    File("input.txt").reader().forEachLine {
        val points = it.split(",").map { i -> i.toLong() }
        coordinates.add(arrayOf(points[0], points[1]))
    }

    var biggestArea = 0L
    coordinates.forEach { c1 ->
        coordinates.forEach { c2 ->
            val newArea = (-1L * (c1[0] - c2[0] + 1)) * (-1L * (c1[1] - c2[1] + 1))
            if (newArea > biggestArea) {
                biggestArea = newArea
            }
        }
    }

    println(biggestArea)

}