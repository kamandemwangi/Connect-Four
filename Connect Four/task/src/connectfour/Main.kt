package connectfour

import java.lang.NumberFormatException

//printing game board start from 0,0
//playing the game starts from
//bottom to top => board.size - 1, 0

const val v = "║"
const val l = "╚"
const val h = "═"
const val c = "╩"
const val r = "╝"
var k = 1
var moves = 0
var board: List<MutableList<Char>> = emptyList()
fun main() {
    val regex = Regex("(\\d+)[xX](\\d+)")
    val boardRange = 5..9
    var rows: Int
    var columns: Int
    println("Connect Four")
    println("First player's name:")
    val firstPlayer = readln()
    println("Second player's name:")
    val secondPlayer = readln()

    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        val boardSize = readln().trim().replace("\\s+".toRegex(), "")
        if (boardSize.isBlank()) {
            println("$firstPlayer VS $secondPlayer")
            println("6 X 7 board")
            board = List(6) { MutableList(7) { ' ' } }
            printBoard(6, 7)
            break
        }
        if (!regex.matches(boardSize)) {
            println("Invalid input")
            continue
        }
        val f = regex.find(boardSize)!!
        rows = f.groupValues[1].toInt()
        columns = f.groupValues[2].toInt()

        if (rows !in boardRange) {
            println("Board rows should be from 5 to 9")
            continue
        }
        if (columns !in boardRange) {
            println("Board columns should be from 5 to 9")
            continue
        }

        println("$firstPlayer VS $secondPlayer")
        println("$rows X $columns board")
        board = List(rows) { MutableList(columns) { ' ' } }
        printBoard(rows, columns)
        break
    }
    play(board, firstPlayer, secondPlayer)
}

fun play(board: List<MutableList<Char>>, firstPlayer: String, secondPlayer: String) {
    val boardRange = 1..board[0].size
    var isFirstPlayerPlaying = true
    var index = board.size
    while (true) {
        if (isFirstPlayerPlaying) println("$firstPlayer's turn:")
        else println("$secondPlayer's turn:")

        val input = readln()
        if (input == "end") {
            println("Game over!")
            break
        }
        if (!input.isInt()) {
            println("Incorrect column number")
            continue
        }
        val column = input.toInt()
        if (column !in boardRange) {
            println("The column number is out of range (1 - ${board[0].size})")
            continue
        }
        //mutable lists are 0 index
        val col = column - 1
        if (board[0][col] == 'o' || board[0][col] == '*') {
            println("Column $column is full")
            continue
        }
        for (i in board) {
            if (board[--index][col] == ' ') {
                if (isFirstPlayerPlaying) {
                    board[index][col] = 'o'
                    isFirstPlayerPlaying = false
                    moves++
                } else {
                    board[index][col] = '*'
                    isFirstPlayerPlaying = true
                    moves++
                }
                printBoard(board.size, board[0].size)
                index = board.size
                break
            }
        }
        if (moves == board.size * board[0].size) {
            println("It is a draw\nGame over!")
            break
        }
        if (winningConditions(board)) {
            if (isFirstPlayerPlaying) {
                println("Player $secondPlayer won\nGame over!")
            } else println("Player $firstPlayer won\nGame over!")
            break
        }
    }

}

fun winningConditions(board: List<MutableList<Char>>): Boolean {
    var count = 1
    //horizontal
    for (row in board) {
        for (j in 0 until row.size - 1) {
            val curr = row[j]
            val next = row[j + 1]
            if (count == 4) return true
            if ((curr != ' ' && next != ' ' ) && curr == next) count++
            else count = 1
        }
        count = 1
    }

    //vertical
    for (i in board[0].indices) {
        for (j in 0 until board.size - 1) {
            val curr = board[j][i]
            val next = board[j + 1][i]
            if ((curr != ' ' && next != ' ' ) && curr == next) count++
            else count = 1
            if (count == 4) return true
        }
        count = 1
    }

    //diagonal
    count = 0
    for (i in board.indices) {
        var n = i
        for (j in board[0].indices) {
            if (j >= board[0].size) continue
            if (n >= board.size) continue
            var prev: Char
            val curr = board[n][j]
            prev = if (j == 0) curr else board[n - 1][j - 1]
            if ((prev != ' ' && curr != ' ') && curr == prev) count++
            else if (prev == ' ' && curr != ' ') count++
            else count = 0
            if (count == 4) return true
            n++
        }
        count = 0
        n = i

        for (j in board[0].size - 1 downTo 0) {
            if (j >= board[0].size) continue
            if (n >= board.size) continue
            var prev: Char
            val curr = board[n][j]
            prev = when (j) {
                board[0].size - 1 -> curr
                else -> board[n - 1][j + 1]
            }
            if ((prev != ' ' && curr != ' ' ) && curr == prev) count++
            else if (prev == ' ' && curr != ' ') count++
            else count = 0
            if (count == 4) return true
            n++
        }
        count = 0
    }
    return false
}

fun printBoard(rows: Int, columns: Int) {
    var n = 0
    var m = 0
    val row = rows + 2
    for (i in 1..row) {
        for (j in 1..columns * 2) {
            when (i) {
                1 -> print(if (j % 2 == 0) k++ else " ")
                row -> if (j == 1) print(l) else if (j == columns * 2) print("$h$r")
                else if (j % 2 == 0) print(h) else print(c)

                else -> print(if (j % 2 == 0) board[n][m++] else v)
            }
        }
        if (i > 1 && i != row) {
            n++
            println(v)
        } else println()
        m = 0
    }
    k = 1
}

fun String.isInt(): Boolean {
    return try {
        this.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}