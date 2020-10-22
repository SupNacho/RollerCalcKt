package rck.supernacho.ru.rollercalckt.screens.utils

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

abstract class DrawableClickListener
constructor(private val drawableIndex: Int, private val fuzz: Int = DEFAULT_FUZZ) : View.OnTouchListener {

    private var drawable: Drawable? = null

    companion object {
        const val DRAWABLE_INDEX_LEFT = 0
        const val DRAWABLE_INDEX_RIGHT = 2

        const val DEFAULT_FUZZ = 10
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val textView = (v as TextView)
        this.drawable = textView.compoundDrawables[drawableIndex]
        if (event.action == MotionEvent.ACTION_DOWN && drawable != null) {
            drawable?.bounds?.let {
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (this.isClickOnDrawable(x, y, v, it, this.fuzz)) {
                    return this.onDrawableClick()
                }
            }
        }
        return false
    }

    abstract fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean

    abstract fun onDrawableClick(): Boolean

    abstract class LeftDrawableClickListener : DrawableClickListener {

        constructor() : super(DRAWABLE_INDEX_LEFT)

        constructor(fuzz: Int) : super(DRAWABLE_INDEX_LEFT, fuzz)

        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.paddingLeft - fuzz) {
                if (x <= view.paddingLeft + drawableBounds.width() + fuzz) {
                    if (y >= view.paddingTop - fuzz) {
                        if (y <= view.height - view.paddingBottom + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    }

    abstract class RightDrawableClickListener : DrawableClickListener {

        constructor() : super(DRAWABLE_INDEX_RIGHT)

        constructor(fuzz: Int) : super(DRAWABLE_INDEX_RIGHT, fuzz)

        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.width - view.paddingRight - drawableBounds.width() - fuzz) {
                if (x <= view.width - view.paddingRight + fuzz) {
                    if (y >= view.paddingTop - fuzz) {
                        if (y <= view.height - view.paddingBottom + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}