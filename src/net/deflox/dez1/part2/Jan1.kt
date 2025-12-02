package net.deflox.dez1.part2

import java.io.File

fun main() {
    var curr = 50
    var zeros = 0

    File("input_orig.txt").reader().forEachLine {
        val direction = it[0]
        val amount = it.substring(1..<it.length).toInt()

        val result = rotate(curr, direction, amount)
        curr = result.result
        zeros += result.zeros

        println("$it => $result")
    }

    println(zeros)
}

fun rotate(start: Int, direction: Char, totalRotations: Int): Result {
    var zeros = totalRotations / 100
    val rotations = totalRotations % 100

    if (direction == 'R') { // ++
        val result = if (start + rotations <= 99) start + rotations else 0 + (rotations - (100 - start))
        if (start + rotations > 99) zeros++
        return Result(result, zeros)
    } else if (direction == 'L') { // --
        val result = if (start - rotations >= 0) start - rotations else 100 - (rotations - start)
        if (start != 0 && start - rotations <= 0) zeros++
        return Result(result, zeros)
    }

    throw IllegalArgumentException()
}

class Result(var result: Int, val zeros: Int) {
    override fun toString(): String {
        return "Result(result=$result, zeros=$zeros)"
    }
}