package rck.supernacho.ru.rollercalckt.screens.custom


import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import kotlinx.android.synthetic.main.input_view.view.*
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.screens.showKeyboard

class InputView : FrameLayout {

    var text: String
        set(value) = et_input.setText(value)
        get() = et_input.text.toString()

    var hint: String
        set(value) {
            tv_hint.text = value
        }
        get() = tv_hint.text.toString()

    val autoCompleteView: AppCompatAutoCompleteTextView
        get() = et_input

    var isCorrectionEnabled = false

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
                     defStyleAttr: Int = R.attr.inputViewStyle,
                     defStyleRes: Int = R.style.InputViewStyle) {

        initLayoutParams()

        val style = getThemeAttribute(attrs) ?: getAttr(defStyleAttr) ?: defStyleRes
        inflate(contextWithAppliedStyle(style), R.layout.input_view, this)

        readAttrs(attrs, defStyleAttr, defStyleRes)

        initButtons()
        initFocusActions()
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
                R.styleable.InputView,
                defStyleAttr,
                defStylesRes
        ).apply {
            try {
                this.getBoolean(R.styleable.InputView_inputCorrection, false).let { isCorrectionEnabled = it }
                this.getString(R.styleable.InputView_inputHint)?.let { tv_hint.text = it }
                this.getInt(R.styleable.InputView_inputType)?.let { et_input.inputType = it }
            } finally {
                recycle()
            }
        }
    }

    private fun initButtons() {
        btn_clear.setOnClickListener {
            et_input.run {
                text?.clear()
                requestFocus()
            }
        }
    }

    private fun initFocusActions() {
        et_input.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                v_backGround.background = getDrawable(R.drawable.bg_input_dark)
                v_hintStarter.background = getDrawable(R.drawable.spacer_dark)
                v_inputStarter.background = getDrawable(R.drawable.spacer_primary)
                tv_hint.setAppearance(R.style.Text_Dark_Hint)
                et_input.setAppearance(R.style.Input_Primary_Input)
                et_input.showKeyboard()
            } else {
                v_backGround.background = getDrawable(R.drawable.bg_input_primary)
                v_hintStarter.background = getDrawable(R.drawable.spacer_primary)
                v_inputStarter.background = getDrawable(R.drawable.spacer_dark)
                tv_hint.setAppearance(R.style.Text_Primary_Hint)
                et_input.setAppearance(R.style.Input_PrimaryDark_Input)
            }
        }
    }

    fun setOnChangeListener(listener: (String) -> String) {
        et_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = listener(p0.toString())
                if (isCorrectionEnabled)
                    et_input.let {
                        it.removeTextChangedListener(this)
                        it.setTextKeepState(input)
                        it.addTextChangedListener(this)
                    }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }
}