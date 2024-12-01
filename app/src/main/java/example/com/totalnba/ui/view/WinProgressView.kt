package example.com.totalnba.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import example.com.totalnba.R

class WinProgressView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val paint = Paint()
    private var sum = 100.0f

    var winHomeFloat = 0.0f
        set(value) {
            field = value

            postInvalidate()
        }

    var winAwayFloat = 0.0f
        set(value) {
            field = value

            postInvalidate()
        }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var currentLeft = (winAwayFloat / sum) * width
        val rightToAdd = (winHomeFloat / sum) * width

        if(winAwayFloat > 50.0f){
            paint.color = ContextCompat.getColor(context, R.color.team_red)
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.team_green_dark))
        } else {
            paint.color = ContextCompat.getColor(context, R.color.team_green_dark)
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.team_red))
        }

        canvas.drawRoundRect(currentLeft, 0f, (currentLeft + rightToAdd).coerceAtMost(width.f),
            dp(10), 100f, 100f, paint)
        currentLeft += rightToAdd
    }
}