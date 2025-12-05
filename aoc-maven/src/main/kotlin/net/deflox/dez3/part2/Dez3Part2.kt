package org.example.net.deflox.dez3.part2

import java.io.File
import java.util.TreeSet

fun main() {

    var sum = 0;

//    File("input.txt").reader().forEachLine {
//        val comparator =
//        val numbers = TreeSet<Number>()
//        it.forEachIndexed { i, c ->
//            val number = c.toString().toInt()
//            if (candidates.isEmpty() || (i+1 != it.length && number > candidates.last()[0])) {
//                candidates.forEach { candidate ->
//                    if (number > candidate[1]) {
//                        candidate[1] = number
//                    }
//                }
//                candidates.add(arrayOf(number, 0))
//            } else {
//                candidates.forEach { candidate ->
//                    if (number > candidate[1]) {
//                        candidate[1] = number
//                    }
//                }
//            }
//        }
//
//        sum += biggest
//
//    }

    println(sum)

}

class Number(val pos: Int, val num: Int) {}