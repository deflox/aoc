package net.deflox.dez7

import java.io.File

fun main() {

    val lines = mutableListOf<MutableList<Char>>()

    File("input.txt").reader().forEachLine {
        lines.add(it.toCharArray().toMutableList())
    }

    val foundPositions = mutableMapOf<Int, MutableSet<Node>>()
    foundPositions.getOrPut(2) { mutableSetOf() }.add(findFirstNode(lines[2]))

    for (i in 2..lines.size - 2 step 2) {

        val nodesToCheck = mutableMapOf<Node, Array<Boolean>>()
        foundPositions[i]?.forEach { node ->
            nodesToCheck[node] = arrayOf(false, false)
        }

        for (y in i + 2..lines.size step 2) {

            if (y == lines.size) {
                nodesToCheck.forEach { entry ->
                    if (!entry.value[0]) {
                        addPosition(y, entry.key.x - 1, entry.key, foundPositions)
                        entry.value[0] = true
                    }
                    if (!entry.value[1]) {
                        addPosition(y, entry.key.x + 1, entry.key, foundPositions)
                        entry.value[1] = true
                    }
                }
            }

            nodesToCheck.forEach { entry ->
                if (!entry.value[0] && lines[y][entry.key.x - 1] == '^') {
                    addPosition(y, entry.key.x - 1, entry.key, foundPositions)
                    entry.value[0] = true
                }
                if (!entry.value[1] && lines[y][entry.key.x + 1] == '^') {
                    addPosition(y, entry.key.x + 1, entry.key, foundPositions)
                    entry.value[1] = true
                }
            }

            if (nodesToCheck.all { entry -> entry.value[0] && entry.value[1] }) break // everything has been found

        }

    }

    val list = mutableListOf<Int>()
    list.add(0)
    countPaths(foundPositions[2]!!.first(), list)
    println(list.size + 1)

}

fun countPaths(node: Node, countingList: MutableList<Int>) {
    val childrenInOrder = node.children.toList().sortedBy { node -> node.x }
    if (!childrenInOrder.isEmpty()) {
        countPaths(childrenInOrder[0], countingList)
        countingList[0] = countingList[0] + 1
        countPaths(childrenInOrder[1], countingList)
    }
}

fun findFirstNode(line: List<Char>): Node {
    line.forEachIndexed { index, ch -> if (ch == '^') return Node(2, index) }
    throw IllegalStateException()
}

fun addPosition(y: Int, x: Int, parent: Node, foundPositions: MutableMap<Int, MutableSet<Node>>) {
    if (!foundPositions.contains(y)) {
        foundPositions[y] = mutableSetOf()
    }

    var node = foundPositions[y]?.firstOrNull { node -> node.x == x }
    if (node == null) {
        node = Node(y, x)
        foundPositions[y]?.add(node)
    }

    parent.children.add(node)
}