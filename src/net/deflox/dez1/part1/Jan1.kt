package net.deflox.dez1.part1

import java.io.File

fun main() {
    var curr = 50
    var zeros = 0;

    File("input_orig.txt").reader().forEachLine {
        val direction = it[0]
        val amount = it.substring(1..<it.length).toInt()

        curr = rotate(curr, direction, amount)

        println("$it => $curr")

        if (curr == 0) {
            zeros++
        }
    }

    println(zeros)
}

fun rotate(start: Int, direction: Char, totalRotations: Int) : Int {
    val rotations = totalRotations % 100

    if (direction == 'R') { // ++
        return if (start + rotations <= 99) start + rotations else 0 + (rotations - (100 - start))
    } else if (direction == 'L') { // --
        return if (start - rotations >= 0) start - rotations else 100 - (rotations - start)
    }

    throw IllegalArgumentException()
}