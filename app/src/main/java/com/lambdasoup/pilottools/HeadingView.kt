package com.lambdasoup.pilottools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class HeadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyle: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...

    private val textPaint: TextPaint
    private val compassPaint: Paint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    init {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.HeadingView, defStyle, 0
        )

        _exampleColor = a.getColor(
            R.styleable.HeadingView_exampleColor,
            exampleColor
        )
        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
        }

        // Update TextPaint and text measurements from attributes
    }

    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(300f, 300f)
        canvas.scale(1.5f, 1.5f)

        canvas.drawCircle(0f, 0f, 100f, compassPaint)

        for (angle in 0 until 360 step 10) {
            when {
                angle % 90 == 0 -> canvas.drawHair(angle)
                angle % 30 == 0 -> canvas.drawSmallHair()
                else -> canvas.drawTinyHair()
            }
            canvas.rotate(10f)
        }
    }

    private fun Canvas.drawHair(angle: Int) {
        drawLine(0f, -80f, 0f, -120f, compassPaint)
        drawText("%03d".format(angle), 0.0f, -140.0f, textPaint)
    }

    private fun Canvas.drawSmallHair() {
        drawLine(0f, -90f, 0f, -110f, compassPaint)
    }

    private fun Canvas.drawTinyHair() {
        drawLine(0f, -95f, 0f, -105f, compassPaint)
    }
}
