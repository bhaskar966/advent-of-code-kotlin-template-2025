
fun main() {

    fun part1(input: List<String>): Int {
        val (shapes, regions) = parseInput(input)

        return regions.count { region ->
            canFitRegion(region, shapes)
        }
    }

//    part1(readInput("Day12_test")).println()
    part1(readInput("Day12")).println()

}

data class Shape(val id: Int, val cells: Set<Pair<Int, Int>>)
data class Region(val width: Int, val height: Int, val required: List<Int>)

fun parseInput(input: List<String>): Pair<Map<Int, Shape>, List<Region>> {
    val regionsStartIndex = input.indexOfFirst { it.contains("x") && it.contains(":") && !it.endsWith(":") }

    val shapesSection = if (regionsStartIndex > 0) {
        input.subList(0, regionsStartIndex)
    } else {
        input
    }

    val regionsSection = if (regionsStartIndex >= 0) {
        input.subList(regionsStartIndex, input.size)
    } else emptyList()

    val shapes = parseShapes(shapesSection)
    val regions = parseRegions(regionsSection)

    return shapes to regions
}

fun parseShapes(lines: List<String>): Map<Int, Shape> {
    val shapes = mutableMapOf<Int, Shape>()
    var i = 0

    while (i < lines.size) {
        val line = lines[i].trim()

        if (line.isBlank()) {
            i++
            continue
        }

        if (line.endsWith(":") && line.length > 1) {
            val idStr = line.dropLast(1)
            if (idStr.toIntOrNull() != null) {
                val id = idStr.toInt()
                val cells = mutableSetOf<Pair<Int, Int>>()

                var row = 0
                i++

                while (i < lines.size) {
                    val cellLine = lines[i]
                    if (cellLine.isBlank() || cellLine.endsWith(":")) break

                    cellLine.forEachIndexed { col, c ->
                        if (c == '#') cells.add(row to col)
                    }
                    row++
                    i++
                }

                shapes[id] = Shape(id, cells)
            } else {
                i++
            }
        } else {
            i++
        }
    }

    return shapes
}

fun parseRegions(lines: List<String>): List<Region> {
    return lines.filter { line ->
        line.isNotBlank() && line.contains("x") && line.contains(":") && !line.endsWith(":")
    }.map { line ->
        val parts = line.split(": ")
        val dims = parts[0].split("x")
        val width = dims[0].toInt()
        val height = dims[1].toInt()
        val required = parts[1].split(" ").map { it.toInt() }
        Region(width, height, required)
    }
}



fun generateOrientations(shape: Shape): List<Set<Pair<Int, Int>>> {

    val orientations = mutableListOf<Set<Pair<Int, Int>>>()
    var current = shape.cells

    for (flip in 0..1){
        for (rotation in 0..3) {
            val minRow = current.minOf { it.first }
            val minCol = current.minOf { it.second }
            val normalized = current.map { (r, c) -> (r - minRow) to (c - minCol)  }.toSet()

            orientations.add(normalized)

            current = current.map { (r, c) -> (c to -r) }.toSet()
        }

        current = current.map { (r, c) -> ( r to -c) }.toSet()

    }

    return orientations.toList()

}

fun canPlace(
    grid: Array<BooleanArray>,
    shape: Set<Pair<Int, Int>>,
    startRow: Int,
    startCol: Int
): Boolean {
    for ((dr, dc) in shape) {
        val r = startRow + dr
        val c = startCol + dc
        if(r !in grid.indices || c !in grid[0].indices || grid[r][c]) return false
    }
    return true
}

fun place(
    grid: Array<BooleanArray>,
    shape: Set<Pair<Int, Int>>,
    startRow: Int,
    startCol: Int,
    mark: Boolean
) {
    for ((dr, dc) in shape) {
        grid[startRow + dr][startCol + dc] = mark
    }
}

fun solve(
    grid: Array<BooleanArray>,
    pieces: List<Int>,
    shapeOrientation: Map<Int, List<Set<Pair<Int, Int>>>>,
    pieceIndex: Int
): Boolean {
    if (pieceIndex >= pieces.size) return true

    val shapeId = pieces[pieceIndex]
    val orientations = shapeOrientation[shapeId] ?: return false

    for (orientation in orientations) {
        for (row in grid.indices) {
            for (col in grid[0].indices) {
                if (canPlace(grid, orientation, row, col)) {
                    place(grid, orientation, row, col, true)

                    if (solve(grid, pieces, shapeOrientation, pieceIndex + 1)) {
                        return true
                    }

                    place(grid, orientation, row, col, false)
                }
            }
        }
    }

    return false
}

fun canFitRegion(region: Region, shapes: Map<Int, Shape>): Boolean {
    val grid = Array(region.height) { BooleanArray(region.width) }

    val pieces = mutableListOf<Int>()
    region.required.forEachIndexed { shapeId, count ->
        repeat(count) { pieces.add(shapeId) }
    }

    if (pieces.isEmpty()) return true

    val totalPieceArea = pieces.sumOf { shapes[it]!!.cells.size }
    val gridArea = region.width * region.height
    if (totalPieceArea > gridArea) return false

    pieces.sortByDescending { shapes[it]!!.cells.size }

    val shapeOrientation = shapes.mapValues { (_, shape) ->
        generateOrientations(shape)
    }

    return solve(grid, pieces, shapeOrientation, 0)
}

