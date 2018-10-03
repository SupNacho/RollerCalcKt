package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import rck.supernacho.ru.rollercalckt.MainActivity

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.controller.CalcController
import rck.supernacho.ru.rollercalckt.controller.Controllable
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.controller.PrefsController
import rck.supernacho.ru.rollercalckt.model.Material
import java.lang.ref.WeakReference

import kotlinx.android.synthetic.main.fragment_calc.view.*

class CalcFragment : Fragment(), View.OnKeyListener, View.OnClickListener, View.OnFocusChangeListener,
                        AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener, IViewUpdate{

    private var cont: Context? = null
    private lateinit var addButton: ImageButton
    private lateinit var resultTextView: TextView
    private lateinit var inputOuterD: EditText
    private lateinit var inputInnD: EditText
    private lateinit var seekIn: SeekBar
    private lateinit var seekOut: SeekBar
    private lateinit var spinner: Spinner
    private lateinit var controller: Controllable
    private lateinit var prefs: PrefsController
    private lateinit var spinnerAdapter: ArrayAdapter<Material>

    private var tempInnD: String = ""
    private var tempOutD: String = ""

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calc, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        prefs = (context as MainActivity).prefsController
        addButton = view.calc_fragment_button_add_material
        addButton.requestFocus()
        addButton.setOnClickListener(this)
        resultTextView = view.calc_fragment_text_view_output
        spinner = view.calc_fragment_spinner_material
        val materials = MainController.getMaterialList()
        spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, materials)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this
        seekIn = view.calc_fragment_seek_inner_d
        seekOut = view.calc_fragment_seek_outer_d
        seekIn.setOnSeekBarChangeListener(this)
        seekOut.setOnSeekBarChangeListener(this)
        inputOuterD = view.calc_fragment_outer_d
        inputInnD = view.calc_fragment_inner_d
        setInputOutD(prefs.getOuterLast())
        setInputInnD(prefs.getInnerLast())
        restoreSeekProgress()
        inputInnD.setOnKeyListener(this)
        inputInnD.onFocusChangeListener = this
        inputOuterD.setOnKeyListener(this)
        inputOuterD.onFocusChangeListener = this
        controller = CalcController(WeakReference<Context>(context), inputInnD, inputOuterD, resultTextView)
        MainController.addUpdateListener(this)
        MainController.setCalcController(controller)
    }

    private fun restoreSeekProgress() {
        seekIn.max = getIntPrefs(prefs.getInnerMax())
        seekOut.max = getIntPrefs(prefs.getOuterMax())
        seekOut.progress = getIntPrefs(prefs.getOuterLast())
        seekIn.progress = getIntPrefs(prefs.getInnerLast())
    }

    private fun getIntPrefs(pref : String) : Int {
        val result: Int
        try {
            result = pref.toInt()
        } catch (e : NumberFormatException){
            return 0
        }
        return result
    }

    private fun onButtonPressed(command: String) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(command)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
            cont = context
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
        fun newInstance(): CalcFragment {
            return CalcFragment()
        }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        when(p0){
            inputInnD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && inputInnD.text.isNotBlank()){
                    val progressVal = getProgressVal(inputInnD)
                    seekIn.progress = progressVal
                    tempInnD = inputInnD.text.toString()
                    prefs.setLastInner(inputInnD.text.toString())
                    controller.getLength()
                    return true
                }
            }
            inputOuterD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && inputOuterD.text.isNotBlank()){
                    val progressVal = getProgressVal(inputOuterD)
                    seekOut.progress = progressVal
                    tempOutD = inputOuterD.text.toString()
                    prefs.setLastOuter(inputOuterD.text.toString())
                    controller.getLength()
                    return true
                }
            }
            else -> {
                Toast.makeText(context, "No such element in listener", Toast.LENGTH_SHORT).show()
            }
        }
        return false
    }

    private fun getProgressVal(input : EditText): Int {
        return try {
            input.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    override fun onNothingSelected(adapter: AdapterView<*>?) {
        adapter?.setSelection(0)
    }

    override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, l: Long) {
        val material = adapter?.getItemAtPosition(pos) as Material
        controller.setThick(material.thickness)
        controller.getLength()
    }

    override fun onClick(p0: View?) {
        when(p0){
            addButton -> {
                onButtonPressed("add_fragment")
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, bool: Boolean) {
        when(seekBar){
            seekIn -> {
                setInputInnD(seekIn.progress.toString())
            }
            seekOut -> {
               setInputOutD(seekOut.progress.toString())
            }
            else -> {
                Toast.makeText(context, "No such SeekBar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        prefs.setLastOuter(inputOuterD.text.toString())
        prefs.setLastInner(inputInnD.text.toString())
        controller.getLength()
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when(p0){
            inputInnD -> {
                if (p1) {
                    tempInnD = inputInnD.text.toString()
                    setInputInnD("")
                } else {
                    setInputInnD(tempInnD)
                }
            }
            inputOuterD -> {
                if (p1) {
                    tempOutD = inputOuterD.text.toString()
                   setInputOutD("")
                } else {
                    setInputOutD(tempOutD)
                }
            }
        }
    }

    override fun updateView() {
        spinnerAdapter.notifyDataSetChanged()
    }

    private fun setInputInnD(string: String?) {
        inputInnD.text = Editable.Factory.getInstance().newEditable(string)
    }
    private fun setInputOutD(string: String?) {
        inputOuterD.text = Editable.Factory.getInstance().newEditable(string)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainController.removeUpdateListener(this)
        (context as MainActivity).getRWatcher().watch(this)
    }
}
