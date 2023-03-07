package connectfour

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
        break
    }
}