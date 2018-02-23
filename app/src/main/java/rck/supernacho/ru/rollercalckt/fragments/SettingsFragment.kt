package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import rck.supernacho.ru.rollercalckt.MainActivity

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.controller.PrefsController

class SettingsFragment : Fragment(), View.OnFocusChangeListener, View.OnKeyListener {

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var tempInnMax = "150"
    private var tempOutMax = "300"
    private lateinit var editTextMaxInnD: EditText
    private lateinit var editTextMaxOutD: EditText
    private lateinit var prefs: PrefsController

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_settings, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        prefs = (context as MainActivity).prefsController
        editTextMaxInnD = view.findViewById(R.id.edit_text_set_inner_d)
        editTextMaxInnD.setOnKeyListener(this)
        editTextMaxInnD.onFocusChangeListener = this
        editTextMaxOutD = view.findViewById(R.id.edit_text_set_outer_d)
        editTextMaxOutD.setOnKeyListener(this)
        editTextMaxOutD.onFocusChangeListener = this
        editTextMaxInnD.text = Editable.Factory.getInstance().newEditable(prefs.getInnerMax())
        editTextMaxOutD.text = Editable.Factory.getInstance().newEditable(prefs.getOuterMax())
    }

    fun onButtonPressed(command: String) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(command)
        }
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(command: String)
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
