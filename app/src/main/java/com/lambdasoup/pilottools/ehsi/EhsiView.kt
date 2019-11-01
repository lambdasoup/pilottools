package com.lambdasoup.pilottools.ehsi

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.lambdasoup.pilottools.R
import com.lambdasoup.pilottools.databinding.EhsiViewBinding
import kotlinx.android.synthetic.main.ehsi_view.view.*

@BindingMethods(
    value = [
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
    ]
)
class EhsiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val value = ObservableField<Ehsi>()
    fun setValue(value: Ehsi) {
        this.value.set(value)
    }

    init {
        val inflater = LayoutInflater.from(context)
        val binding: EhsiViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.ehsi_view, this, true
        )
        binding.value = value
    }

    fun setOnTurnRightListener(listener: () -> Unit) {
        turn_right.setOnClickListener { listener.invoke() }
    }

    fun setOnTurnLeftListener(listener: () -> Unit) {
        turn_left.setOnClickListener { listener.invoke() }
    }
}
