package com.lambdasoup.pilottools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation

class HeadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val prop = object : FloatPropertyCompat<HeadingView>("value") {
        override fun setValue(view: HeadingView, value: Float) {
            heading = value
            invalidate()
        }

        override fun getValue(view: HeadingView): Float {
            return heading
        }
    }
    private val dampener = SpringAnimation(this, prop)

    private var heading: Float = 0.0f

    fun setValue(value: Float) {
        dampener.animateToFinalPosition(value)
    }

    private val textPaint: TextPaint
    private val compassPaint: Paint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    private val compassRect = RectF()

    init {
        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val p = 20f
        compassRect.set(p, p, width - p, height - p)
        canvas.save()
        canvas.clipRect(compassRect)
        drawCompass(canvas)
    }

    private fun drawCompass(canvas: Canvas) {
        val bounds = canvas.clipBounds

        canvas.translate(bounds.exactCenterX(), bounds.exactCenterY())
        canvas.rotate(-heading)

        val minSize = arrayOf(bounds.width(), bounds.height()).min()!!
        canvas.scale(minSize / 350f, minSize / 350f)

        canvas.drawCircle(0f, 0f, 100f, compassPaint)

        for (angle in 0 until 360 step 10) {
            when {
                angle % 90 == 0 -> drawHair(canvas, angle)
                angle % 30 == 0 -> drawSmallHair(canvas)
                else -> drawTinyHair(canvas)
            }
            canvas.rotate(10f)
        }
    }

    private fun drawHair(canvas: Canvas, angle: Int) {
        canvas.drawLine(0f, -80f, 0f, -120f, compassPaint)
        canvas.drawText("%03d".format(angle), 0.0f, -140.0f, textPaint)
    }

    private fun drawSmallHair(canvas: Canvas) {
        canvas.drawLine(0f, -90f, 0f, -110f, compassPaint)
    }

    private fun drawTinyHair(canvas: Canvas) {
        canvas.drawLine(0f, -95f, 0f, -105f, compassPaint)
    }
}
