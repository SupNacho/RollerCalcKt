package rck.supernacho.ru.rollercalckt.screens.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.calculation_view.view.*
import rck.supernacho.ru.rollercalckt.R

class CalculationView : FrameLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }


    private fun init(attrs: AttributeSet? = null,
                     defStyleAttr: Int = R.attr.calculationViewStyle,
                     defStyleRes: Int = R.style.CalculationViewStyle) {

        initLayoutParams()

        val style = getThemeAttribute(attrs) ?: getAttr(defStyleAttr) ?: defStyleRes
        inflate(contextWithAppliedStyle(style), R.layout.calculation_view, this)

        readAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initLayoutParams() {
        layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        )
    }

    @SuppressLint("Recycle")
    private fun readAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStylesRes: Int) {
        context.obtainStyledAttributes(
                attrs,
                R.styleable.CalculationView,
                defStyleAttr,
                defStylesRes
        ).apply {
            try {

            } finally {
                recycle()
            }
        }
    }

    fun setData(length: String, weight: String, yard: String, isYardEnabled: Boolean, isWeightEnabled: Boolean) {
        tv_lengthCalculation.text = length
        tv_weightCalculation.text = weight
        tv_yardCalculation.text = yard
        tv_yardCalculation.setVisibility(isYardEnabled)
        _v_yardStarter.setVisibility(isYardEnabled)
        tv_weightCalculation.setVisibility(isWeightEnabled)
        _v_delimiter.setVisibility(isWeightEnabled || isYardEnabled)
    }
}