package com.lambdasoup.pilottools

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import kotlinx.android.synthetic.main.ehsi_view.view.*

@BindingMethods(value = [
    BindingMethod(
        type = EhsiView::class,
        attribute = "app:onTurnRight",
        method = "setOnTurnRightListener"
    ),
    BindingMethod(
        type = EhsiView::class,
        attribute = "app:onTurnLeft",
        method = "setOnTurnLeftListener"
    )
])
class EhsiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var heading: Float = 0.0f
    set(value) {
        field = value
        heading_view.heading = value
        heading_text.text = "%.0f".format(value)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.ehsi_view, this)
    }

    fun setOnTurnRightListener(listener: () -> Unit) {
        turn_right.setOnClickListener { listener.invoke() }
    }

    fun setOnTurnLeftListener(listener: () -> Unit) {
        turn_left.setOnClickListener { listener.invoke() }
    }
}
