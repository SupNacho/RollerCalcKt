package rck.supernacho.ru.rollercalckt.screens.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_preference.*
import kotlinx.android.synthetic.main.view_status_popup.view.*
import rck.supernacho.ru.rollercalckt.R

class StatusPupUp : FrameLayout {
    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(ctx, attrs, defStyleAttr, 0)
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(ctx, attrs, defStyleAttr, defStyleRes)

    init {
        View.inflate(context, R.layout.view_status_popup, this)
    }

    fun show(resId: Int? = null) {
        resId?.let {
            iv_icon.setImageResource(it)
        }
        val animation : ViewPropertyAnimator
        fl_saved.alpha = 0f
        fl_saved.run {
            animation = animate().run {
                alpha(1f)
                interpolator = AccelerateInterpolator()
                startDelay = 35L
                setDuration(500L)
            }
        }
        fl_saved.visibility = View.VISIBLE
        animation.start()
        fl_saved.postDelayed({ fl_saved?.visibility = View.GONE }, 1500)

    }
}