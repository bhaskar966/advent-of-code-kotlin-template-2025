
fun main() {

    fun part1(input: List<Pair<String, String>>): Long {

        val ranges = input.map { it.first.toLong()..it.second.toLong() }
        val invalidIds = mutableSetOf<Long>()

        val maxDigits = ranges.maxOfOrNull { it.last.toString().length } ?: 0

        var firstHalf = 1L
        while (true) {
            val s = firstHalf.toString()

            if (s.length * 2 > maxDigits + 1 && maxDigits > 0) break

            val repeatedS = s + s
            val invalidId = repeatedS.toLongOrNull() ?: break

            for(range in ranges) {
                if(invalidId in range){
                    invalidIds.add(invalidId)
                }
            }
            firstHalf++
        }

        return invalidIds.sum()

    }

    fun part2(input: List<Pair<String, String>>): Long {
        val ranges = input.map { it.first.toLong()..it.second.toLong() }
        val invalidIds = mutableSetOf<Long>()

        val maxDigits = ranges.maxOfOrNull { it.last.toString().length } ?: 0

        var firstHalf = 1L
        while (true) {
            val s = firstHalf.toString()

            if (s.length * 2 > maxDigits + 1 && maxDigits > 0) break

            var repetitions = 2
            while (true) {
                val repeatedS = s.repeat(repetitions)

                if(repeatedS.length > maxDigits && maxDigits > 0) break

                val invalidId = repeatedS.toLongOrNull() ?: break

                for(range in ranges) {
                    if(invalidId in range){
                        invalidIds.add(invalidId)
                    }
                }
                repetitions++
            }
            firstHalf++
        }

        return invalidIds.sum()
    }


//    part1(readInputAndParsePair("Day02")).println()
//    part1(readInputAndParsePair("Day02_test")).println()

//    part2(readInputAndParsePair("Day02")).println()
    part2(readInputAndParsePair("Day02_test")).println()

}