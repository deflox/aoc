package net.deflox.dez11

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
        val node = allNodes.getOrPut(nodeAndChildren[0].trim()) { Node(nodeAndChildren[0].trim()) }
        node.children.addAll(children)
        node.children.forEach { it.parents[node.name] = node }
    }

    allNodes.values.forEach { node ->
        node.children.forEach { child ->
            node.pathsToEnd[child.name] = 0L
        }
    }

    val path = mutableListOf<String>()

    val start1 = allNodes["svr"]!!
    init(allNodes)
    search(start1, path, "fft")
    println(start1.pathsToEnd.values.reduce { a, b -> a + b })

    val start2 = allNodes["fft"]!!
    init(allNodes)
    search(start2, path, "dac")
    println(start2.pathsToEnd.values.reduce { a, b -> a + b })

    val start3 = allNodes["dac"]!!
    init(allNodes)
    search(start3, path, "out")
    println(start3.pathsToEnd.values.reduce { a, b -> a + b })

}

fun init(allNodes: MutableMap<String, Node>) {
    allNodes.values.forEach { node ->
        node.children.forEach { child ->
            node.pathsToEnd[child.name] = 0L
        }
    }
}

fun search(node: Node, currentPath: MutableList<String>, search: String) {
    currentPath.add(node.name)
    if (node.fullyAnalysed) {
        markPath(if (node.pathsToEnd.isEmpty()) 1 else node.pathsToEnd.values.reduce { a, b -> a + b }, currentPath, node)
        currentPath.removeLast()
        return
    } else if (node.name == search) {
        markPath(1, currentPath, node)
    }
    if (!node.children.isEmpty()) {
        node.children.forEach { search(it, currentPath, search) }
    }
    node.fullyAnalysed = true
    currentPath.removeLast()
}

fun markPath(increment: Long, currentPath: MutableList<String>, from: Node) {
    var current = from
    for (i in currentPath.size - 2 downTo 0) {
        current.parents[currentPath[i]]!!.pathsToEnd[currentPath[i + 1]] = current.parents[currentPath[i]]!!.pathsToEnd[currentPath[i + 1]]!! + increment
        current = current.parents[currentPath[i]]!!
    }
}

class Node(val name: String, val children: MutableSet<Node> = mutableSetOf(), val parents: MutableMap<String, Node> = mutableMapOf(), var fullyAnalysed: Boolean = false, val pathsToEnd: MutableMap<String, Long> = mutableMapOf()) {
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