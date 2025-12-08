

fun main() {

    fun part1(input: List<String>, pairsToConnect: Int = 1000): Long {
        val points = parsePoints(input)
        val n = points.size

        val edges = buildAllEdges(points)
        val dsu = DisjointSet(n)

        for(i in 0 until minOf(pairsToConnect, edges.size)) {
            val e = edges[i]
            dsu.union(e.u, e.v)
        }

        val sizes = dsu.componentSize().sortedDescending()
        require(sizes.size >= 3) { "Need at least 3 circuits to compute the product." }

        val a = sizes[0].toLong()
        val b = sizes[1].toLong()
        val c = sizes[2].toLong()

        return a * b * c
    }

    fun part2(input: List<String>): Long {
        val points = parsePoints(input)
        val n = points.size

        val edges = buildAllEdges(points)
        val dsu = DisjointSet(n)

        for (edge in edges) {
            val rx = dsu.find(edge.u)
            val ry = dsu.find(edge.v)
            if (rx != ry) {
                dsu.union(rx, ry)
                if (dsu.countComponents() == 1) {
                    val x1 = points[edge.u].x
                    val x2 = points[edge.v].x
                    return x1 * x2
                }
             }
        }

        error("Should have found a single circuit")
    }


//    part1(readInput("Day08_test"), 10).println()
//    part1(readInput("Day08")).println()
    part2(readInput("Day08")).println()

}