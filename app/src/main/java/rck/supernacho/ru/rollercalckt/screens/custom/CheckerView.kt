package rck.supernacho.ru.rollercalckt.screens.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.checker_view.view.*
import rck.supernacho.ru.rollercalckt.R

class CheckerView : FrameLayout {

    var isChecked: Boolean = false
        set(value) {
            field = value
            if (value)
                btn_checked?.setBackgroundResource(R.drawable.spacer_active_small)
            else
                btn_checked?.setBackgroundResource(R.drawable.spacer_dark_small)
        }

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
                     defStyleAttr: Int = R.attr.checkerViewStyle,
                     defStyleRes: Int = R.style.CheckerViewStyle) {

        initLayoutParams()

        val style = getThemeAttribute(attrs) ?: getAttr(defStyleAttr) ?: defStyleRes
        inflate(contextWithAppliedStyle(style), R.layout.checker_view, this)

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
                R.styleable.CheckerView,
                defStyleAttr,
                defStylesRes
        ).apply {
            try {
                this.getString(R.styleable.CheckerView_checkerText)?.let { et_input.text = it }
                this.getBoolean(R.styleable.CheckerView_checkerCheck, false).let { isChecked = it }
            } finally {
                recycle()
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cl_root.setOnClickListener {
            if (!isChecked) {
                isChecked = true
                l?.onClick(this)
            }
        }
    }

    fun setCheckerCheck(check: Boolean) {
        isChecked = check
    }

}