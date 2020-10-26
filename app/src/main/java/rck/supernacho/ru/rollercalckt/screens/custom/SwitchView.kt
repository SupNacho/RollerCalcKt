package rck.supernacho.ru.rollercalckt.screens.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.switch_view.view.*
import rck.supernacho.ru.rollercalckt.R

class SwitchView : FrameLayout {

    var isChecked: Boolean = swt_limit?.isChecked ?: false
        set(value) {
            swt_limit?.isChecked = value
            field = value
        }
        get() = swt_limit?.isChecked ?: false

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
                     defStyleAttr: Int = R.attr.switchViewStyle,
                     defStyleRes: Int = R.style.SwitchViewStyle) {

        initLayoutParams()

        val style = getThemeAttribute(attrs) ?: getAttr(defStyleAttr) ?: defStyleRes
        inflate(contextWithAppliedStyle(style), R.layout.switch_view, this)

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
                R.styleable.SwitchView,
                defStyleAttr,
                defStylesRes
        ).apply {
            try {
                this.getString(R.styleable.SwitchView_switchText)?.let { tv_description.text = it }
                this.getBoolean(R.styleable.SwitchView_switchChecked, false).let { swt_limit.isChecked = it }
            } finally {
                recycle()
            }
        }
    }

    fun setSwitchChecked(isChecked: Boolean) {
        this.isChecked = isChecked
    }

    override fun setOnClickListener(l: OnClickListener?) {
        swt_limit.setOnClickListener(l)
    }
}