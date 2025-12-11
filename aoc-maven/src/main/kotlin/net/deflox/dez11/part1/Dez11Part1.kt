package org.example.net.deflox.dez11.part1

import java.io.File

fun main() {
    val allNodes = mutableMapOf<String, Node>()
    File("input.txt").reader().forEachLine { line ->
        val nodeAndChildren = line.split(":")
        val children = nodeAndChildren[1].trim().split(" ").map { childStr ->
            if (allNodes.containsKey(childStr.trim())) {
                allNodes[childStr.trim()]!!
            } else {
                allNodes[childStr.trim()] = Node(childStr.trim())
                allNodes[childStr.trim()]!!
            }
        }
        val node = allNodes.getOrPut(nodeAndChildren[0].trim(), { Node(nodeAndChildren[0].trim()) })
        node.children.addAll(children)
    }

    val youNode = allNodes["svr"]!!
    val countArray = arrayOf(0L)
    searchPart1(youNode, countArray)

    println(countArray[0])

}

fun searchPart1(node: Node, count: Array<Long>) {
    if (node.name == "out") {
        count[0] = count[0] + 1
    }
    if (!node.children.isEmpty()) {
        node.children.forEach { searchPart1(it, count) }
    }
}

class Node(val name: String, val children: MutableSet<Node> = mutableSetOf()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Node(name='$name')"
    }


}