package rck.supernacho.ru.rollercalckt.screens.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.*
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import rck.supernacho.ru.rollercalckt.screens.utils.DrawableClickListener
import java.io.Serializable
import kotlin.math.max
import kotlin.math.roundToInt

fun Context.getColorFromRes(colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Resources.getColorFromRes(colorRes: Int): Int = ResourcesCompat.getColor(this, colorRes, null)

fun View.getString(res: Int): String = context.getString(res)

fun TextView.setAppearance(styleRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        this.setTextAppearance(styleRes)
    else
        this.setTextAppearance(context, styleRes)
}

fun View.getColor(colorRes: Int): Int = context.getColorFromRes(colorRes)

fun View.getDrawable(drawableRes: Int): Drawable? = ContextCompat.getDrawable(context, drawableRes)

fun View.getDimenPx(dimenRes: Int): Int = context.resources.getDimensionPixelSize(dimenRes)

fun Context.getAttr(attr: Int): Int? = if (attr == 0) {
    null
} else {
    val value = TypedValue()
    if (this.theme.resolveAttribute(attr, value, true)) {
        if (value.resourceId == 0) null else value.resourceId
    } else {
        null
    }
}

fun View.getAttr(attr: Int): Int? = this.context.getAttr(attr)

fun View.contextWithAppliedStyle(parentStyleRes: Int): Context =
        ContextThemeWrapper(context, parentStyleRes)

fun Context.applyStyle(styleRes: Int): Context = ContextThemeWrapper(this, styleRes)

fun Context.applyStyleFromAttrOrRes(styleAttr: Int, styleRes: Int): Context =
        ContextThemeWrapper(this, this.getAttr(styleAttr) ?: styleRes)

@SuppressLint("Recycle")
fun View.getThemeAttribute(attrs: AttributeSet?): Int? {

    if (attrs == null) return null

    context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.theme)).apply {
        return try {
            getResourceId(0, -1).let { if (it == -1) null else it }
        } finally {
            recycle()
        }
    }
}

fun View.getThemedContext(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int): Context {

    val theme: Int? = context.obtainStyledAttributes(
            attrs,
            intArrayOf(android.R.attr.theme),
            defStyleAttr,
            defStyleRes
    ).run {
        try {
            getResourceId(0, -1).let { if (it == -1) null else it }
        } finally {
            recycle()
        }
    }

    return if (theme != null) ContextThemeWrapper(context, theme) else context
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun <T : View> T.runWhenHasSize(func: T.() -> Unit) {
    if (width > 0 || height > 0) {
        func.invoke(this)
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                func.invoke(this@runWhenHasSize)
            }
        })
    }
}

fun FragmentManager.clearBackStack() {
    if (this.backStackEntryCount > 0) {
        val firstEntryId = this.getBackStackEntryAt(0).id
        this.popBackStack(firstEntryId, POP_BACK_STACK_INCLUSIVE)
    }
}

fun FragmentManager.findBackStackEntry(entryName: String): FragmentManager.BackStackEntry? {
    for (i in 0 until this.backStackEntryCount) {
        val entry = this.getBackStackEntryAt(i)
        if (entry.name == entryName) return entry
    }
    return null
}

fun FragmentManager.popBackStack(fragment: Class<out Fragment>): Boolean {
    val result = this.findBackStackEntry(fragment.name)?.let {
        this.popBackStack(it.name, 0)
    }
    return result != null
}

fun FragmentManager.showFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean = true) =
        this.beginTransaction()
                .replace(containerId, fragment, fragment::class.java.name)
                .also { transaction ->
                    if (addToBackStack) transaction.addToBackStack(fragment::class.java.name)
                }
                .commitAllowingStateLoss()

fun FragmentManager.showFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean = true, sharedElement: View, transName: String) =
        this.beginTransaction()
                .replace(containerId, fragment, fragment::class.java.name)
                .also { transaction ->
                    transaction.addSharedElement(sharedElement, transName)
                    if (addToBackStack) transaction.addToBackStack(fragment::class.java.name)
                }
                .commitAllowingStateLoss()

fun FragmentManager.addFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean) =
        this.beginTransaction()
                .add(containerId, fragment, fragment::class.java.name).also { transition ->
                    if (addToBackStack) transition.addToBackStack(fragment::class.java.name)
                }
                .commitAllowingStateLoss()


fun String.spanFromHtml(): Spanned {
    var text = this
    if (text.contains("<li>"))
        text = text.replace("<li>".toRegex(), "\t•  ").replace("</li>".toRegex(), "<br>")
    return Html.fromHtml(text)
}

fun <T : Enum<T>> Bundle.addEnum(key: String, clazz: T): Bundle {
    this.putString(key, clazz.name)
    return this
}

fun Bundle.addAllFrom(from: Bundle): Bundle = this.apply { putAll(from) }

inline fun <reified T : Enum<T>> Bundle.getEnum(key: String): T? {
    val typeStr: String? = this.getString(key)
    return enumValues<T>().find { it.name == typeStr }
}

fun Bundle.addString(key: String, value: String): Bundle {
    this.putString(key, value)
    return this
}

fun Bundle.addInt(key: String, value: Int): Bundle {
    this.putInt(key, value)
    return this
}

fun Bundle.addLong(key: String, value: Long): Bundle {
    this.putLong(key, value)
    return this
}

fun Bundle.addParcelable(key: String, value: Parcelable): Bundle {
    this.putParcelable(key, value)
    return this
}

fun Bundle.addSerializable(key: String, value: Serializable): Bundle {
    this.putSerializable(key, value)
    return this
}

inline fun <reified T : Enum<T>> valueOfSafe(value: String?): T? =
        value?.let { enumValues<T>().find { it.name == value } }

fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun Context.convertDpToPixel(dp: Float): Int =
        (dp * (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()

fun Context.convertPixelsToDp(pixel: Float): Int =
        (pixel / (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()

fun Context.getScreenWidthPx(): Int {
    val metrics = DisplayMetrics()

    (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay
            .getMetrics(metrics)

    return metrics.widthPixels
}

fun Context.getScreenHeightPx(): Int {
    val metrics = DisplayMetrics()

    (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay
            .getMetrics(metrics)

    return metrics.heightPixels
}

fun View.setBottomPadding(paddingPx: Int) = this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, paddingPx)

fun View.convertDpToPixel(dp: Float): Int = context.convertDpToPixel(dp)

fun View.convertPixelToDp(pixel: Float): Int = context.convertPixelsToDp(pixel)

fun Context.convertSpToPixel(sp: Float): Int =
        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.resources.displayMetrics))

fun Collection<View>.setVisibitity(visibile: Int) {
    this.forEach {
        it.visibility = visibile
    }
}

fun Fragment.fitsSystemWindowWithInsets() = view?.fitsSystemWindowWithInsets()

fun View.fitsSystemWindowWithInsets() {
    doOnApplyWindowInsets { _, windowInsets, _ ->
        if (layoutParams.height > 0) layoutParams.height = layoutParams.height + windowInsets.systemWindowInsetTop
        setPadding(paddingLeft, paddingTop + windowInsets.systemWindowInsetTop, paddingRight, paddingBottom)
        setOnApplyWindowInsetsListener(null)
    }
}

fun String.fromHtml(): Spanned =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(this, 0)
        else
            Html.fromHtml(this)

/**
 * Extension for processing system insets like status bar, keyboard and etc.
 *
 * @see requestApplyInsetsWhenAttached
 * @see recordInitialPaddingForView
 * @see InitialPadding
 */
fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    // Create initial padding for view
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener with lambda from params
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        // This function need to return WindowsInsets
        insets
    }
    // request initial Insets
    requestApplyInsetsWhenAttached()
}

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // If we already attached
        requestApplyInsets()
    } else {
        // If we're not attached to the hierarchy,
        // add a listener to request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
        view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

fun ViewGroup.setIsChildrenEnabled(isEnabled: Boolean) {
    for (i in 0 until this.childCount) {
        this.getChildAt(i).let {
            it.isEnabled = isEnabled
            if (it is ViewGroup) it.setIsChildrenEnabled(isEnabled)
        }
    }
}

fun TabLayout.addOnTabSelectionListener(listener: (tab: TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) {}
        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabSelected(tab: TabLayout.Tab) {
            listener.invoke(tab)
        }
    })
}

/**
 * Extension to set click listener to a list of views.
 * Useful when a fragment implements View.OnClickListener
 */
fun List<View>.setClickListeners(listener: View.OnClickListener) =
        this.forEach { it.setOnClickListener(listener) }


fun View.buttonEnabled(enabled: Boolean, disabledAlpha: Float = 0.6f) {
    this.isClickable = enabled
    this.alpha = if (enabled) 1f else disabledAlpha
}

sealed class TextLink {
    abstract val text: String
    abstract val onClickListener: (View) -> Unit
}

data class UrlLink(
        override val text: String,
        val link: String,
        override val onClickListener: (View) -> Unit = { view ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            view.context.startActivity(browserIntent)
        }
) : TextLink()

data class ActionTextLink(
        override val text: String,
        override var onClickListener: (View) -> Unit
) : TextLink()




fun TextView.makeBold(vararg links: String) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val boldSpan = StyleSpan(Typeface.BOLD)
        val startIndexOfLink = this.text.toString().indexOf(link)
        spannableString.setSpan(boldSpan, startIndexOfLink, startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    }
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TypedArray.getResourceId(attrIndex: Int): Int? =
        getResourceId(attrIndex, -1).let { if (it != -1) it else null }

fun TypedArray.getInt(attrIndex: Int): Int? =
        getInt(attrIndex, -1).let { if (it != -1) it else null }

fun TypedArray.getColor(attrIndex: Int): Int? =
        getColor(attrIndex, -1).let { if (it != -1) it else null }

fun TextView.setTextAppearanceRes(res: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(res)
    } else {
        this.setTextAppearance(context, res)
    }
}

fun applyAlpha(color: Int, alpha: Float): Int {
    val alphaAsByte: Int = Math.round(Color.alpha(color) * alpha)
    val red: Int = Color.red(color)
    val green: Int = Color.green(color)
    val blue: Int = Color.blue(color)
    return Color.argb(alphaAsByte, red, green, blue)
}

fun View.getLocationOnScreen(): IntArray {
    val loc = IntArray(2)
    getLocationOnScreen(loc)
    return loc
}

fun View.getLocationInWindow(): IntArray {
    val loc = IntArray(2)
    getLocationInWindow(loc)
    return loc
}

fun makeShareIntent(sharedBody: String) =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, sharedBody)
        }

fun openDialerWithPhone(phone: String) =
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(phone)
        }


fun TextView.setOnLeftDrawableClick(f: () -> Unit) {
    setOnTouchListener(object : DrawableClickListener.LeftDrawableClickListener() {
        override fun onDrawableClick(): Boolean {
            f()
            return true
        }
    })
}

fun TextView.setOnRightDrawableClick(f: () -> Unit) {
    setOnTouchListener(object : DrawableClickListener.RightDrawableClickListener() {
        override fun onDrawableClick(): Boolean {
            f()
            return true
        }
    })
}

/**
 * Set click listener to all first level children of a ViewGroup
 * @param tag used to set listener only to views with specific tag. Otherwise listener will be set on every child.
 * */
fun ViewGroup.setClickListenersOnChildren(listener: View.OnClickListener, tag: String? = null) {
    for (i in 0 until this.childCount) {
        if (tag == null) this.getChildAt(i).setOnClickListener(listener)
        else if (tag == this.getChildAt(i).tag) this.getChildAt(i).setOnClickListener(listener)
    }
}

/**
 * Function for show sides item inside viewPager
 *
 * @param pageMarginPx - margin of page in px
 * @param offsetPx - margin of page offset in px
 */
fun ViewPager2.setShowSideItems(pageMarginPx: Int, offsetPx: Int) {

    clipToPadding = false
    clipChildren = false
    offscreenPageLimit = 3

    setPageTransformer { page, position ->

        val offset = position * -(2 * offsetPx + pageMarginPx)
        if (this.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }
    }

}

fun TabLayout.adjustScrollableOrFixedModeAutomatically() {
    this.tabMode = TabLayout.MODE_SCROLLABLE

    // Не оптимальное решение. Количество табов может остаться прежним, но их вьюхи могут быть
    // других размеров. Для оптимального решения нужно сделать эту обработку в BcsTabLayout
    // (с учётом установки в него view pager) и исользовать BcsTabLayout везде
    var previousTabCount = 0

    this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        if (this.tabCount != previousTabCount) {
            previousTabCount = this.tabCount

            var totalWidth = 0
            var maxWidth = 0
            for (i in 0 until this.tabCount) {
                val tabWidth = (this.getChildAt(0) as ViewGroup).getChildAt(i)?.width ?: 0
                totalWidth += tabWidth
                maxWidth = max(maxWidth, tabWidth)
            }
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
            if (totalWidth < screenWidth && this.tabCount != 0) {
                this.tabMode = TabLayout.MODE_FIXED
            } else {
                this.tabMode = TabLayout.MODE_SCROLLABLE
            }
        }
    }
}
