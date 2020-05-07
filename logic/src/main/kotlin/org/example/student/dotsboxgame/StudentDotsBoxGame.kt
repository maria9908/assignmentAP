package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.Matrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.SparseMatrix
import kotlin.random.Random

class StudentDotsBoxGame (width: Int, height: Int, players: List<Player>): AbstractDotsAndBoxesGame() {

    //storing the players in a list and making a copy of it
    override val players: List<Player> = players.map{it}

    //putting index non the current player
    private var currentPlayerIndex: Int = 0

    override val currentPlayer: Player
        get()= players[currentPlayerIndex ]


    // NOTE: you may want to me more specific in the box type if you use that type in your class
    override val boxes: Matrix<StudentBox > = MutableMatrix( width, height, ::StudentBox )

    override val lines : SparseMatrix<StudentLine> = MutableSparseMatrix( width+1,height * 2 - 1,::StudentLine) {
            x , y -> y  % 2 == 1 ||  x< width}

    override var isFinished= false


    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override val isDrawn = false


        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {

                var leftBoxX = lineX+lineY
                var rightBoxX = lineX+lineY
                var boxY = lineY

                return Pair(boxes[leftBoxX, boxY], boxes[rightBoxX, boxY])

            }

        override fun drawLine() {
            if (isDrawn){
                throw IllegalStateException("A line is already drawn here")
            }

             for (box in boxes)
            {
                if (box.boundingLines.all { line -> line.isDrawn })
                {
                }
            }

            fireGameChange()


            TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player.
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>

            get() {
                val right = lines[boxX+1,boxY]
                val left = lines [boxX-1, boxY]
                val top= lines [boxX ,boxY-1]
                val bottom = lines [boxX,boxY+1]

                return mutableListOf (right,left,top,bottom)

            }

    }
}