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
    }

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