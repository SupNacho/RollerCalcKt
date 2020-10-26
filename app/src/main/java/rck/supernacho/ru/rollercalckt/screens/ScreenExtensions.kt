package rck.supernacho.ru.rollercalckt.screens

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.ArrowConstraints
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.screens.custom.getColor
import rck.supernacho.ru.rollercalckt.screens.utils.DrawableClickListener

fun Balloon.Builder.setBalloonSettings(v: View,viewLifecycleOwner: LifecycleOwner, text: String, withOverlay: Boolean = true) {
    val positionArray = IntArray(2)
    v.getLocationOnScreen(positionArray)
    setOverlayPosition(Point(positionArray[0], positionArray[1] - (v.height * 1.5).toInt()))
    setIsVisibleOverlay(withOverlay)
    setWidth(((v.parent as View).width * 0.2).toInt())
    setMarginLeft(16)
    setMarginTop(16)
    setMarginBottom(16)
    setPadding(16)
    overlayShape = BalloonOverlayRoundRect(12f, 12f)
    setArrowOrientation(ArrowOrientation.RIGHT)
    setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
    setOverlayPadding(2f)
    setCornerRadius(4f)
    setLifecycleOwner(viewLifecycleOwner)
    setBalloonAnimation(BalloonAnimation.FADE)
    setBackgroundColorResource(R.color.colorAccent)
    setOverlayColorResource(R.color.balloonOverlayBg)
    setText(text)
    setTextGravity(Gravity.START)
    setTextColor(v.getColor(R.color.colorPrimaryDark))
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun TextView.setOnRightDrawableClick(f: () -> Unit) {
    setOnTouchListener(object : DrawableClickListener.RightDrawableClickListener() {
        override fun onDrawableClick(): Boolean {
            f()
            return true
        }
    })
}

fun EditText.showKeyboard() = (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, 0)

fun View.setKeyboardShownListener(listener: (Boolean) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener{
        val heightDiff = rootView.height - height
        listener(heightDiff > 250)
    }
}