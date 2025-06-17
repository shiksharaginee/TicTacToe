package com.example.tictactoe

import android.widget.Button
import android.app.AlertDialog
import android.os.Bundle
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Typeface
import android.graphics.Color
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var statusText: TextView
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private var playerTurn = true // true = X, false = O or AI
    private var isAIMode = false
    private var aiDifficulty = "Easy" // or "Hard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusText = findViewById(R.id.statusText)
        statusText.setTextColor(Color.GREEN)
        val restartButton = findViewById<Button>(R.id.restartButton)
        restartButton.setOnClickListener { resetGame() }
        restartButton.setTextColor(Color.parseColor("#C4A484"))

        gridLayout = findViewById(R.id.gridLayout)

        showGameModeDialog()
    }

    private fun showGameModeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Game Mode")
        builder.setMessage("Do you want to play against a friend or AI?")
        builder.setPositiveButton("Two Player") { _, _ ->
            isAIMode = false
            setupBoard()
        }
        builder.setNegativeButton("Play vs Computer") { _, _ ->
            isAIMode = true
            showMode()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun setupBoard() {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val button = Button(this)
                button.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 250
                    setMargins(5, 5, 5, 5)
                }
                button.textSize = 32f
                button.setTypeface(null, Typeface.BOLD)
                button.setTextColor(Color.parseColor("#C19A6B"))
                button.setBackgroundColor(Color.parseColor("#411900"))
                button.setOnClickListener { onCellClick(i, j) }
                gridLayout.addView(button)
                buttons[i][j] = button
            }
        }

        statusText.setTextColor(Color.GREEN)
        playerTurn = true
        statusText.text = "Player X's Turn"
    }

    private fun showMode() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Level")
        builder.setMessage("Do you want to play easy level or hard?")
        builder.setPositiveButton("Easy level") { _, _ ->
            aiDifficulty = "Easy"
            setupBoard()
        }
        builder.setNegativeButton("Hard level") { _, _ ->
            aiDifficulty = "Hard"
            setupBoard()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun onCellClick(row: Int, col: Int) {
        val button = buttons[row][col] ?: return

        if (button.text.isNotEmpty()) return

        button.text = if (playerTurn) "X" else "O"
        if (checkWin()) {
            statusText.text = if (playerTurn) "Player X Wins!" else "Player O Wins!"
            disableBoard()
            return
        }

        if (isDraw()) {
            statusText.text = "It's a Draw!"
            return
        }

        playerTurn = !playerTurn
        statusText.text = "Player ${if (playerTurn) "X" else if (isAIMode) "Computer" else "O"}'s Turn"

        if (isAIMode && !playerTurn) {
            aiMove()
        }
    }

    private fun aiMove() {
        val (row, col) = if (aiDifficulty == "Easy") {
            val emptyCells = mutableListOf<Pair<Int, Int>>()
            for (i in 0..2) {
                for (j in 0..2) {
                    if (buttons[i][j]?.text.isNullOrEmpty()) {
                        emptyCells.add(Pair(i, j))
                    }
                }
            }
            emptyCells.random()
        } else {
            AI.bestMove(buttons)
        }

        buttons[row][col]?.text = "O"

        if (checkWin()) {
            statusText.text = "Computer Wins!"
            disableBoard()
            return
        }

        if (isDraw()) {
            statusText.text = "It's a Draw!"
            return
        }

        playerTurn = true
        statusText.text = "Player X's Turn"
    }

    private fun isDraw(): Boolean {
        return buttons.all { row -> row.all { btn -> btn?.text?.isNotEmpty() == true } }
    }

    private fun checkWin(): Boolean {
        // Check rows, columns, diagonals
        for (i in 0..2) {
            if (buttons[i][0]?.text == buttons[i][1]?.text &&
                buttons[i][0]?.text == buttons[i][2]?.text &&
                buttons[i][0]?.text!!.isNotEmpty()
            ) return true

            if (buttons[0][i]?.text == buttons[1][i]?.text &&
                buttons[0][i]?.text == buttons[2][i]?.text &&
                buttons[0][i]?.text!!.isNotEmpty()
            ) return true
        }

        if (buttons[0][0]?.text == buttons[1][1]?.text &&
            buttons[0][0]?.text == buttons[2][2]?.text &&
            buttons[0][0]?.text!!.isNotEmpty()
        ) return true

        if (buttons[0][2]?.text == buttons[1][1]?.text &&
            buttons[0][2]?.text == buttons[2][0]?.text &&
            buttons[0][2]?.text!!.isNotEmpty()
        ) return true

        return false
    }

    private fun disableBoard() {
        for (row in buttons) {
            for (btn in row) {
                btn?.isEnabled = false
            }
        }
    }
    private fun resetGame() {
        for (row in buttons) {
            for (button in row) {
                button?.text = ""
                button?.isEnabled = true
            }
        }
        showGameModeDialog()
    }
}
