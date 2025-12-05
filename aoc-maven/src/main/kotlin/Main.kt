package org.example

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    var sum = 0;

    File("input.txt").reader().forEachLine {
        val candidates = mutableListOf<Array<Int>>()
        it.forEachIndexed { i, c ->
            val number = c.toString().toInt()
            if (candidates.isEmpty() || (i+1 != it.length && number > candidates.last()[0])) {
                candidates.forEach { candidate ->
                    if (number > candidate[1]) {
                        candidate[1] = number
                    }
                }
                candidates.add(arrayOf(number, 0))
            } else {
                candidates.forEach { candidate ->
                    if (number > candidate[1]) {
                        candidate[1] = number
                    }
                }
            }
        }

        var biggest = 0
        for (candidate in candidates) {
            val candidateInt = (candidate[0].toString() + candidate[1].toString()).toInt()
            if (candidateInt > biggest) {
                biggest = candidateInt;
            }
        }

        sum += biggest

    }

    println(sum)

}