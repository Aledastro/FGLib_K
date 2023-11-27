package com.uzery.fglib.utils.math.solve

import com.uzery.fglib.utils.data.debug.DebugData

class SolveBlock(
    private val defaultValue: SolveNumber,
    private val expression: String,
    private val resolve: (String) -> SolveNumber
) {
    private val operators = arrayListOf(
        arrayListOf("+", "-"),
        arrayListOf("*", "/", "//", "%"),
        arrayListOf("^"),
        arrayListOf("|"),
        arrayListOf("(", ")")
    )

    fun solve(): String {
        return solved().expression
    }

    private lateinit var next_operator: String

    private fun solved(): SolveBlock {
        if (operators.all { list -> list.all { it !in expression } }) return this

        for (ops in operators) {
            val blocks = getBlocks(ops)

            if (blocks.size == 1 || blocks.size == 2 && blocks[0] in ops) continue

            var result = defaultValue
            next_operator = "+"
            for (block in blocks) {
                val solvedBlock = SolveBlock(defaultValue, block, resolve).solved()
                val exp = solvedBlock.expression

                if (exp in ops) {
                    next_operator = exp
                } else {
                    result = applyOperator(result, resolve(exp), next_operator)
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
        for (i in 0 until list.size-1) {
            res.add(expression.substring(list[i], list[i+1]))
        }
        return res
    }
}
