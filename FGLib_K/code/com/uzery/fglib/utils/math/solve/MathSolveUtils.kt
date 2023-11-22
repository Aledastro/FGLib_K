package com.uzery.fglib.utils.math.solve

object MathSolveUtils {
    fun solveInt(expression: String): Int {
        return try {
            expression.toInt()
        } catch (e: NumberFormatException) {
            SolveBlock(SolveInt(0), expression) { SolveInt(it.toInt()) }.solve().toInt()
        }
    }

    fun solveDouble(expression: String): Double {
        return try {
            expression.toDouble()
        } catch (e: NumberFormatException) {
            SolveBlock(SolveDouble(0.0), expression) { SolveDouble(it.toDouble()) }.solve().toDouble()
        }
    }

    fun solveLong(expression: String): Long {
        return try {
            expression.toLong()
        } catch (e: NumberFormatException) {
            SolveBlock(SolveLong(0), expression) { SolveLong(it.toLong()) }.solve().toLong()
        }
    }
}
