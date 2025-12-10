package net.deflox.dez10

import java.io.File
import java.util.*

fun main() {
    val inputs = mutableListOf<Input>()
    File("input.txt").reader().forEachLine {
        val split = it.split(" ")
        inputs.add(Input(parseTargetLights(split[0]), parseButtons(split.subList(1, split.size - 1), split[0].length - 2)))
    }

    var total = 0

    inputs.forEach { input ->

        val queue = LinkedList<Number>()
        queue.add(Number(0, 0, null)) // root
        while (!queue.isEmpty()) {

            val number = queue.poll()
            input.buttons.forEach { queue.add(Number(it, 0, number)) }
            if (number.parent != null) {
                number.progress = number.parent.progress xor number.value
                if (number.progress == input.target) {
                    var count = 0
                    var curr = number
                    while (curr.parent != null) {
                        count += 1
                        curr = curr.parent
                    }
                    total += count
                    break
                }
            }

        }

    }

    println(total)

}

class Number(val value: Int, var progress: Int, val parent: Number?)

class Input(val target: Int, val buttons: List<Int>)

fun parseTargetLights(lights: String): Int {
    return Integer.parseInt(lights.substring(1, lights.length - 1).toCharArray().joinToString("") { if (it == '.') "0" else "1" }, 2)
}

fun parseButtons(buttons: List<String>, len: Int): List<Int> {
    return buttons.map { parseButton(it, len) }
}

fun parseButton(button: String, len: Int): Int {
    val range = 0..<len
    val numbers = button.substring(1, button.length - 1).split(",").map { it.toInt() }.toSet()
    return Integer.parseInt(range.joinToString("") { if (numbers.contains(it)) "1" else "0" }, 2)
}