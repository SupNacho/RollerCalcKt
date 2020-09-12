package rck.supernacho.ru.rollercalckt.controller

import android.content.Context
import android.view.View
import android.widget.*
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.calc.Calculator
import java.lang.ref.WeakReference
@Deprecated("old_calculator")
//class CalcController(val context: Context, private vararg val views: View) : Controllable {
class CalcController(val contextRef: WeakReference<Context>, private vararg val views: View) : Controllable {
    private val context = contextRef.get()
    private val calculator = Calculator()
    private var out: Int = 0
    private var inn: Int = 0
    var thickness: Double = 0.215

    override fun setOuterD(outD: Int) {
        out = outD
    }

    override fun setInnerD(innD: Int) {
        inn = innD
    }

    override fun setThick(thick: Double) {
        thickness = thick
    }

    override fun getLength() {
        for (view in views) {
            if (view is EditText && view.id == R.id.calc_fragment_inner_d && !view.text.isNullOrEmpty()) {
                inn = view.text.toString().toInt()
            }
            if (view is EditText && view.id == R.id.calc_fragment_outer_d  && !view.text.isNullOrEmpty()) {
                out = view.text.toString().toInt()
            }
        }
        val result = calculator.getLength(out, inn, thickness) + context?.resources?.getString(R.string.calc_out_unit_text_view)
        views
                .filterIsInstance<TextView>()
                .filter { it.id == R.id.calc_fragment_text_view_output }
                .forEach { it.text = result}
    }
}