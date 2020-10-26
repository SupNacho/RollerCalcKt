package rck.supernacho.ru.rollercalckt.screens.custom

import android.annotation.SuppressLint
import android.content.Context
import android.text.style.LineHeightSpan
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.input_view.view.*
import kotlinx.android.synthetic.main.sort_view.view.*
import rck.supernacho.ru.rollercalckt.R

class SortView : FrameLayout {

    var isWeightEnabled: Boolean = false
        set(value) {
            field = value
            iv_densitySort.setVisibility(isVisible = value)
            iv_weightSort.setVisibility(isVisible = value)
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
        iv_nameSort.setOnClickListener {
            setActiveSort(isName = true)
            action()
        }
    }

    fun setThickSortAction(action: () -> Unit) {
        iv_thickSort.setOnClickListener {
            setActiveSort(isThick = true)
            action()
        }
    }

    fun setWeightSortAction(action: () -> Unit) {
        iv_weightSort.setOnClickListener {
            setActiveSort(isWeight = true)
            action()
        }
    }

    fun setDensitySortAction(action: () -> Unit) {

        iv_densitySort.setOnClickListener {
            setActiveSort(isDensity = true)
            action()
        }
    }

    private fun setActiveSort(isName: Boolean = false, isThick: Boolean = false, isWeight: Boolean = false, isDensity: Boolean = false) {
        val (sortDescription, backGround) = when {
            isName -> {
                setIcon(nameIcon = R.drawable.ic_name_active)
                R.string.sort_by_name to R.drawable.spacer_active_small
            }
            isThick -> {
                setIcon(thickIcon = R.drawable.ic_thick_active)
                R.string.sort_by_thick to R.drawable.spacer_active_small
            }
            isWeight -> {
                setIcon(weightIcon = R.drawable.ic_weight_active)
                R.string.sort_by_weight to R.drawable.spacer_active_small
            }
            isDensity -> {
                setIcon(densityIcon = R.drawable.ic_density_active)
                R.string.sort_by_density to R.drawable.spacer_active_small
            }
            else -> {
                setIcon()
                R.string.sort_by to R.drawable.spacer_dark_small
            }
        }

        tv_sortDescription.setText(sortDescription)
        v_sortDescription.setBackgroundResource(backGround)
        v_sortDescriptionStarter.setBackgroundResource(R.drawable.spacer_dark)
        tv_sortDescription.setAppearance(R.style.Text_Dark_Hint)

    }

    private fun setIcon(
            nameIcon: Int = R.drawable.ic_name_sort,
            thickIcon: Int = R.drawable.ic_thick_sort,
            weightIcon: Int = R.drawable.ic_weight_sort,
            densityIcon: Int = R.drawable.ic_density_sort) {
        iv_nameSort.setImageResource(nameIcon)
        iv_thickSort.setImageResource(thickIcon)
        iv_weightSort.setImageResource(weightIcon)
        iv_densitySort.setImageResource(densityIcon)
    }
}