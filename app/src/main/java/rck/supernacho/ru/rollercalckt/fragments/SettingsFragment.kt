package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.view.*
import rck.supernacho.ru.rollercalckt.MainActivity

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.controller.PrefsController

class SettingsFragment : Fragment(), View.OnFocusChangeListener, View.OnKeyListener {

    private var tempInnMax = "150"
    private var tempOutMax = "300"
    private lateinit var editTextMaxInnD: EditText
    private lateinit var editTextMaxOutD: EditText
    private lateinit var prefs: PrefsController

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        prefs = (context as MainActivity).prefsController
        editTextMaxInnD = view.edit_text_set_inner_d
        editTextMaxInnD.setOnKeyListener(this)
        editTextMaxInnD.onFocusChangeListener = this
        editTextMaxOutD = view.edit_text_set_outer_d
        editTextMaxOutD.setOnKeyListener(this)
        editTextMaxOutD.onFocusChangeListener = this
        editTextMaxInnD.text = Editable.Factory.getInstance().newEditable(prefs.getInnerMax())
        editTextMaxOutD.text = Editable.Factory.getInstance().newEditable(prefs.getOuterMax())
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0) {
            editTextMaxInnD -> {
                if (p1) {
                    tempInnMax = editTextMaxInnD.text.toString()
                    setInputInnD("")
                } else {
                    setInputInnD(tempInnMax)
                }
            }
            editTextMaxOutD -> {
                if (p1) {
                    tempOutMax = editTextMaxOutD.text.toString()
                    setInputOutD("")
                } else {
                    setInputOutD(tempOutMax)
                }
            }
        }
    }

    private fun setInputInnD(string: String) {
        editTextMaxInnD.text = Editable.Factory.getInstance().newEditable(string)
    }

    private fun setInputOutD(string: String) {
        editTextMaxOutD.text = Editable.Factory.getInstance().newEditable(string)
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        when (p0) {
            editTextMaxInnD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && editTextMaxInnD.text.isNotBlank()) {
                    tempInnMax = editTextMaxInnD.text.toString()
                    prefs.setInnerMax(tempInnMax)
                    return true
                }
            }
            editTextMaxOutD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && editTextMaxOutD.text.isNotBlank()) {
                    tempOutMax = editTextMaxOutD.text.toString()
                    prefs.setOuterMax(tempOutMax)
                    return true
                }
            }
            else -> {
                Toast.makeText(context, "No such element in listener", Toast.LENGTH_SHORT).show()
            }
        }
        return false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(command: String)
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
