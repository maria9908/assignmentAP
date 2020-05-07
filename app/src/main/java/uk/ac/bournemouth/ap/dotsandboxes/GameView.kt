package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer

class GameView : View, DotsAndBoxesGame.GameChangeListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //creating a game object
    var game  : StudentDotsBoxGame =
        StudentDotsBoxGame(7, 6, listOf(HumanPlayer(), HumanPlayer())).also { it.addOnGameChangeListener(this) }

    //overriding the setter
    set(value:StudentDotsBoxGame) {
        field.removeOnGameChangeListener(this)
        field = value
        value.addOnGameChangeListener(this)
    }


    //declaring the variables
    private var widthCount = game.boxes.width
    private val heightCount =7
    private var mGridPaint: Paint
    private var mNoPlayerPaint: Paint
    private var mPlayer1Line: Paint
    private var mPlayer2Line: Paint

    private val myGestureDetector = GestureDetector(context, myGestureListener())

    //setting up paint objects
    init {
        mGridPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT

        }
        mNoPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK

            mPlayer1Line = Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
            }
            mPlayer2Line = Paint().apply {
                style = Paint.Style.FILL
                color = Color.RED
            }
        }


    }

    //onDraw method to paint the design on the canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val chosenDiameter: Float
        var LineDrawn: Int
        var paint: Paint
        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()
        val diameterX: Float = viewWidth / heightCount.toFloat()
        val diameterY: Float = viewHeight / widthCount .toFloat()

        if (diameterX < diameterY)
            chosenDiameter = diameterX
        else
            chosenDiameter = diameterY

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mGridPaint)

        val radius = chosenDiameter / 2 /4

        // Draw the dots on the game board
        for (col in 0 until heightCount) {
            for (row in 0 until widthCount ) {
                paint = mNoPlayerPaint

                // Calculate the co-ordinates of the circle
                val cx = chosenDiameter * col + radius
                val cy = chosenDiameter * row + radius
                canvas.drawCircle(cx, cy, radius, paint)

                //draw a line after getting the coordinates from gesture touch
                //canvas.drawLine(canvas)
            }
        }

    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return myGestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev)

    }

    inner class myGestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(ev: MotionEvent): Boolean {
            return true
        }
         override fun onSingleTapUp(ev: MotionEvent): Boolean {

            var turn=game.currentPlayer

            // Work out the width of each column in pixels
            val colWidth = width/heightCount
            val rowWidth = height/widthCount

            // Calculate the column and row number from the X co-ordinate of the touch event
             var x1 = ev.x.toInt()/colWidth
             var y1= ev.y.toInt()/colWidth
             var x2 = ev.x.toInt()/rowWidth
             var y2= ev.y.toInt()/rowWidth

             //game.drawLine(x1,y1,x2,y2, mPlayer1Line)
             invalidate()

            return true
        }



    }
    companion object { // declare a constant (must be in the companion)
        const val LOGTAG = "MyTask"
    }

    override fun onGameChange(game: DotsAndBoxesGame) {
        invalidate()
    }


}

