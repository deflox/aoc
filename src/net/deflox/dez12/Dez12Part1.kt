package net.deflox.dez12

import java.io.File

fun main() {

    val lines = mutableListOf<String>()
    File("input.txt").reader().forEachLine { lines.add(it) }
    val shapes = lines.mapIndexedNotNull { i, e -> i.takeIf { e.matches(Regex("\\d:"))} }
        .zip(lines.mapIndexedNotNull { i, e -> i.takeIf { e == "" } })
        .map { lines.subList(it.first + 1, it.second).map { l -> l.toCharArray() } }
        .mapIndexed { i, s -> s.map { l -> l.map { c -> if (c == '#') (65+i).toChar() else c } } }
    val areas = lines.subList(lines.indexOfFirst { it.matches(Regex("\\d\\dx\\d\\d.*")) }, lines.size)
        .map { line ->
            val width = line.split(":")[0].split("x")[0].toInt()
            val height = line.split(":")[0].split("x")[1].toInt()
            val surface = mutableListOf<MutableList<Char>>()
            for (y in 0..<height) {
                val line = mutableListOf<Char>()
                for (x in 0..<width) {
                    line.add('.')
                }
                surface.add(line)
            }
            Area(width, height, surface, line.split(":")[1].trim().split(" ").map { it -> it.trim().toInt() }.toList())
        }

    var possible = 0

    areas.forEach { area ->

        var totalAreaNeeded = area.requirements[1] * 9 + area.requirements[5] * 9
        totalAreaNeeded += if (area.requirements[0] % 2 == 0) {
            (area.requirements[0] / 2) * 15
        } else {
            ((area.requirements[0] - 1) / 2) * 15 + 9
        }
        totalAreaNeeded += if (area.requirements[4] % 2 == 0) {
            (area.requirements[4] / 2) * 12
        } else {
            ((area.requirements[4] - 1) / 2) * 12 + 9
        }

        // 2 & 3
        val pairs = if (area.requirements[2] > area.requirements[3]) area.requirements[3] else area.requirements[2]
        totalAreaNeeded += pairs * 15
        totalAreaNeeded += if (pairs == area.requirements[2]) (area.requirements[3] - pairs) * 9 else (area.requirements[2] - pairs) * 9

        if (totalAreaNeeded < (area.height * area.width)) {
            possible += 1
        }

    }

    println(possible)

}

class Area(val width: Int, val height: Int, val surface: MutableList<MutableList<Char>>, val requirements: List<Int>) {
    fun add(x: Int, y: Int, shape: List<List<Char>>) {
        for (h in 0..<shape.size) {
            for (v in 0..<shape[h].size) {
                surface[y + h][x + v] = shape[h][v]
            }
        }
    }

    fun print() {
        surface.forEach { l ->
            println(l.joinToString(""))
        }
    }
}