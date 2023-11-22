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

    private var next_operator = ""

    private fun solved(): SolveBlock {
        for (ops in operators.reversed()) {
            val blocks = getBlocks(ops)

            if (blocks.size == 1) continue

            var result = defaultValue
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
        val breaks = ArrayList<Int>()
        breaks.add(0)
        operators.forEach { op->
            for (i in expression.indices){
                if(expression.substring(0, i).startsWith(op)) breaks.add(i+op.length)
            }
        }
        breaks.add(expression.length)
        val res = ArrayList<String>()
        for (i in 0 until breaks.size-1){
            res.add(expression.substring(breaks[i], breaks[i+1]))
        }
        return res
    }
}
