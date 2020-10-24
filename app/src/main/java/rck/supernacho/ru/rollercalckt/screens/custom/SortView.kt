package rck.supernacho.ru.rollercalckt.screens.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.input_view.view.*
import kotlinx.android.synthetic.main.sort_view.view.*
import rck.supernacho.ru.rollercalckt.R

class SortView : FrameLayout {

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
                     defStyleAttr: Int = R.attr.sortViewStyle,
                     defStyleRes: Int = R.style.SortViewStyle) {

        initLayoutParams()

        val style = getThemeAttribute(attrs) ?: getAttr(defStyleAttr) ?: defStyleRes
        inflate(contextWithAppliedStyle(style), R.layout.sort_view, this)

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
                R.styleable.SortView,
                defStyleAttr,
                defStylesRes
        ).apply {
            try {
                this.getString(R.styleable.SortView_hint)?.let { ivet_find.hint = it }
            } finally {
                recycle()
            }
        }
    }

    fun setOnChangeListener(action: (String) -> Unit) {
        ivet_find.setOnChangeListener {
            action(it)
            it
        }
    }

    fun setNameSortAction(action: () -> Unit) {
        iv_nameSort.setOnClickListener { action() }
    }

    fun setThickSortAction(action: () -> Unit) {
        iv_thickSort.setOnClickListener { action() }
    }

    fun setWeightSortAction(action: () -> Unit) {
        iv_weightSort.setOnClickListener { action() }
    }

    fun setDensitySortAction(action: () -> Unit) {
        iv_densitySort.setOnClickListener { action() }
    }
}