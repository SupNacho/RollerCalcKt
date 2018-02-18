package rck.supernacho.ru.rollercalckt.controller

import android.content.Context
import android.view.View
import android.widget.*
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.calc.Calculator

class CalcController(context: Context, vararg views: View) : Controllable {
    val views = views
    val calculator = Calculator()
    var out: Int = 0
    var inn: Int = 0
    var thickness: Double = 0.215
    val context = context

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
            if (view is EditText && view.id == R.id.calc_fragment_inner_d) inn = view.text.toString().toInt()
            if (view is EditText && view.id == R.id.calc_fragment_outer_d) out = view.text.toString().toInt()
        }
        var result = calculator.getLength(out, inn, thickness)
        for (view in views){
            if (view is TextView && view.id == R.id.calc_fragment_text_view_output) view.text = result
        }
    }
}