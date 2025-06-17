package com.example.tictactoe
import android.widget.*

object AI {
    fun bestMove(board: Array<Array<Button?>>): Pair<Int, Int> {
        var bestScore = Int.MIN_VALUE
        var move = Pair(-1, -1)

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j]?.text.isNullOrEmpty()) {
                    board[i][j]?.text = "O"
                    val score = minimax(board, 0, false)
                    board[i][j]?.text = ""
                    if (score > bestScore) {
                        bestScore = score
                        move = Pair(i, j)
                    }
                }
            }
        }

        return move
    }

    private fun minimax(board: Array<Array<Button?>>, depth: Int, isMaximizing: Boolean): Int {
        val winner = getWinner(board)
        if (winner != null) {
            return when (winner) {
                "X" -> -10 + depth
                "O" -> 10 - depth
                "Draw" -> 0
                else -> 0
            }
        }

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j]?.text.isNullOrEmpty()) {
                        board[i][j]?.text = "O"
                        val score = minimax(board, depth + 1, false)
                        board[i][j]?.text = ""
                        bestScore = maxOf(score, bestScore)
                      }
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j]?.text.isNullOrEmpty()) {
                        board[i][j]?.text = "X"
                        val score = minimax(board, depth + 1, true)
                        board[i][j]?.text = ""
                        bestScore = minOf(score, bestScore)
                    }
                }
            }
            return bestScore
        }
    }

    private fun getWinner(board: Array<Array<Button?>>): String? {
        for (i in 0..2) {
            if (board[i][0]?.text == board[i][1]?.text &&
                board[i][1]?.text == board[i][2]?.text &&
                board[i][0]?.text!!.isNotEmpty()
            ) return board[i][0]?.text.toString()

            if (board[0][i]?.text == board[1][i]?.text &&
                board[1][i]?.text == board[2][i]?.text &&
                board[0][i]?.text!!.isNotEmpty()
            ) return board[0][i]?.text.toString()
        }

        if (board[0][0]?.text == board[1][1]?.text &&
            board[1][1]?.text == board[2][2]?.text &&
            board[0][0]?.text!!.isNotEmpty()
        ) return board[0][0]?.text.toString()

        if (board[0][2]?.text == board[1][1]?.text &&
            board[1][1]?.text == board[2][0]?.text &&
            board[0][2]?.text!!.isNotEmpty()
        ) return board[0][2]?.text.toString()

        if (board.all { row -> row.all { btn -> btn?.text?.isNotEmpty() == true } })
            return "Draw"

        return null
    }
}
