package com.uzery.fglib.utils.math.solve

import com.uzery.fglib.utils.data.debug.DebugData

class SolveBlock(
    private val defaultValue: SolveNumber,
    private val expression: String,
    private val resolve: (String) -> SolveNumber
) {
    companion object {
        private val operators = arrayListOf(
            arrayListOf("+", "-"),
            arrayListOf("+-"),
            arrayListOf("*", "/", "%"),
            arrayListOf("//"),
            arrayListOf("^"),
            /*arrayListOf("|"), //todo
            arrayListOf("(", ")") //todo*/
        )
        private val full_ops = ArrayList<String>()

        init {
            operators.forEach { full_ops.addAll(it) }
        }
    }

    fun solve(): String {
        return solved().expression
    }

    private fun solved(): SolveBlock {
        if (operators.all { list -> list.all { it !in expression } }) return this

        fun checkForDoubleOpsOrEndOp(): Boolean {
            val blocks_un = getBlocks(full_ops)
            var last = false
            var last_block_un = ""

            if (blocks_un.size == 1) return false

            if (blocks_un.last() in full_ops) return true

            for (block_un in blocks_un) {
                val now = block_un in full_ops

                val double_op = last_block_un+block_un !in listOf("+-", "//")
                if (last && now && double_op) return true
                last = now
                last_block_un = block_un
            }

            return false
        }
        if (checkForDoubleOpsOrEndOp()) return ERROR

        var next_operator: String
        for (ops in operators) {
            val blocks = getBlocks(ops)

            if (blocks.size == 1 || blocks.size == 2 && blocks[0] in ops) continue

            var result = defaultValue
            next_operator = "+"

            for (block in blocks) {
                val solved_block = SolveBlock(defaultValue, block, resolve).solved()

                when (val exp = solved_block.expression) {
                    ERROR_EXPRESSION -> {
                        return ERROR
                    }

                    in ops -> {
                        next_operator = exp
                    }

                    else -> {
                        result = applyOperator(result, resolve(exp), next_operator)
                    }
                }
            }
            return SolveBlock(defaultValue, result.expression, resolve)
        }
        return this
    }

    private fun applyOperator(first: SolveNumber, second: SolveNumber, operator: String): SolveNumber {
        return when (operator) {
            "+" -> first+second
            "-" -> first-second

            "*" -> first*second
            "/" -> first/second
            "%" -> first%second

            "//" -> first.divFull(second)

            "^" -> first.pow(second)

            else -> throw DebugData.error("wrong operator: $operator")
        }
    }

    private fun getBlocks(operators: ArrayList<String>): ArrayList<String> {
        val breaks = HashSet<Int>()
        breaks += 0
        operators.forEach { op ->
            for (i in expression.indices) {
                if (expression.substring(i).startsWith(op)) {
                    breaks += i
                    breaks += i+op.length
                }
            }
        }
        breaks += expression.length

        val list = breaks.toList().sorted()
        val res = ArrayList<String>()
        for (i in 0..<list.size-1) {
            res.add(expression.substring(list[i], list[i+1]))
        }
        return res
    }

    private val ERROR_EXPRESSION = "ERROR"
    private val ERROR: SolveBlock
        get() = SolveBlock(this.defaultValue, ERROR_EXPRESSION, this.resolve)
}
