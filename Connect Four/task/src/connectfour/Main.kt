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
var games = 0
var immutableGamesNumber = 0
var rounds = 1
val map = HashMap<String, Int>()
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

    map[firstPlayer] = 0
    map[secondPlayer] = 0

    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        val boardSize = readln().trim().replace("\\s+".toRegex(), "")
        if (boardSize.isBlank()) {
            initializeGame(
                    rows = 6,
                    columns = 7,
                    firstPlayer = firstPlayer,
                    secondPlayer = secondPlayer
            )
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
        initializeGame(
                rows = rows,
                columns = columns,
                firstPlayer = firstPlayer,
                secondPlayer = secondPlayer
        )
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

    while (games > 0) {
        while (true) {
            if (isFirstPlayerPlaying) println("$firstPlayer's turn:")
            else println("$secondPlayer's turn:")

            val input = readln()
            if (input == "end") {
                println("Game over!")
                return
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
                if (immutableGamesNumber == 1) {
                    println("It is a draw\n Game over!")
                    return
                }
                draw(
                        firstPlayer = firstPlayer,
                        secondPlayer = secondPlayer
                )
                break
            }
            if (winningConditions(board)) {
                if (isFirstPlayerPlaying) {
                    if (immutableGamesNumber == 1) {
                        println("Player $secondPlayer won")
                        println("Game over!")
                        return
                    }
                    win(
                            player = secondPlayer,
                            firstPlayer = firstPlayer,
                            secondPlayer = secondPlayer
                    )
                } else {
                    if (immutableGamesNumber == 1) {
                        println("Player $firstPlayer won")
                        println("Game over!")
                        return
                    }
                    win(
                            player = firstPlayer,
                            firstPlayer = firstPlayer,
                            secondPlayer = secondPlayer
                    )
                }
                break
            }
        }
        games--
    }

}

fun winningConditions(board: List<MutableList<Char>>): Boolean {
    var count = 1
    //horizontal
    for (row in board) {
        for (j in 0 until row.size - 1) {
            val curr = row[j]
            val next = row[j + 1]
            if ((curr != ' ' && next != ' ' ) && curr == next) count++
            else count = 1
            if (count == 4) return true
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

fun win(player: String, firstPlayer: String, secondPlayer: String) {
    val s = map[player]!!
    map[player] = s + 2
    println("Player $player won")
    println("Score")
    println("$firstPlayer: ${map[firstPlayer]} $secondPlayer: ${map[secondPlayer]}")

    if (games > 1) {
        println("Game #${++rounds}")
        board.forEach { it.fill(' ') }
        printBoard(board.size, board[0].size)
        moves = 0
    } else println("Game over!")

}

fun draw(firstPlayer: String, secondPlayer: String) {
    map[firstPlayer] = map[firstPlayer]!! + 1
    map[secondPlayer] = map[secondPlayer]!! + 1
    println("It is a draw")
    println("Score")
    println("$firstPlayer: ${map[firstPlayer]} $secondPlayer: ${map[secondPlayer]}")

    if (games > 1) {
        println("Game #${++rounds}")
        board.forEach { it.fill(' ') }
        printBoard(board.size, board[0].size)
        moves = 0
    } else println("Game over!")
}
fun initializeGame(rows: Int, columns: Int, firstPlayer: String, secondPlayer: String) {
    while (true) {
        println("Do you want to play single or multiple games?\nFor a single game, input 1 or press Enter\nInput a number of games:")
        val input = readln()
        if (input.isBlank()){
            games = 1
            immutableGamesNumber = games
            break
        }
        if (!input.isInt() || input.toInt() <= 0) {
            println("Invalid input")
        } else {
            games = input.toInt()
            immutableGamesNumber = games
            break
        }
    }

    println("$firstPlayer VS $secondPlayer")
    println("$rows X $columns board")
    if (games == 1) println("Single game")
    else {
        println("Total $games games")
        println("Game #$rounds")
    }
}