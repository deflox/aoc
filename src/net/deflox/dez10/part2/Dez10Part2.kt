package net.deflox.dez10.part2

import java.io.File

fun main() {
    val inputs = mutableListOf<Input>()
    File("input.txt").reader().forEachLine {
        val split = it.split(" ")

        println("Buttons: ${split.subList(1, split.size - 1).size}, Targets: ${split[split.size - 1].split(",").size}")

    }

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