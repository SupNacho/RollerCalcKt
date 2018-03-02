package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
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

class CalcFragment : Fragment(), View.OnKeyListener, View.OnClickListener, View.OnFocusChangeListener,
                        AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener{

    private var mParam1: String? = null
    private var mParam2: String? = null
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

    private var tempInnD: String = ""
    private var tempOutD: String = ""

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
        val view = inflater!!.inflate(R.layout.fragment_calc, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        prefs = (context as MainActivity).prefsController
        addButton = view.findViewById(R.id.calc_fragment_button_add_material)
        addButton.requestFocus()
        addButton.setOnClickListener(this)
        resultTextView = view.findViewById(R.id.calc_fragment_text_view_output)
        spinner = view.findViewById(R.id.calc_fragment_spinner_material)
        val materials = MainController.getMaterialList()
        val spinnerAdapter = ArrayAdapter<Material>(context, android.R.layout.simple_list_item_1, materials)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = this
        seekIn = view.findViewById(R.id.calc_fragment_seek_inner_d)
        seekOut = view.findViewById(R.id.calc_fragment_seek_outer_d)
        seekIn.setOnSeekBarChangeListener(this)
        seekOut.setOnSeekBarChangeListener(this)
        inputOuterD = view.findViewById(R.id.calc_fragment_outer_d)
        inputInnD = view.findViewById(R.id.calc_fragment_inner_d)
        setInputOutD(prefs.getOuterLast())
        setInputInnD(prefs.getInnerLast())
        restoreSeekProgress()
        inputInnD.setOnKeyListener(this)
        inputInnD.onFocusChangeListener = this
        inputOuterD.setOnKeyListener(this)
        inputOuterD.onFocusChangeListener = this
        controller = CalcController(context, inputInnD, inputOuterD, resultTextView)
        MainController.setAdapterCalcFragment(spinnerAdapter)
        MainController.setCalcController(controller)
    }

    private fun restoreSeekProgress() {
        seekIn.max = prefs.getInnerMax().toInt()
        seekOut.max = prefs.getOuterMax().toInt()
        if (Build.VERSION.SDK_INT >= 26) seekOut.min = seekIn.max
        seekOut.progress = prefs.getOuterLast().toInt()
        seekIn.progress = prefs.getInnerLast().toInt()
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
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): CalcFragment {
            val fragment = CalcFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        when(p0){
            inputInnD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && inputInnD.text.isNotBlank()){
                    seekIn.progress = inputInnD.text.toString().toInt()
                    tempInnD = inputInnD.text.toString()
                    prefs.setLastInner(inputInnD.text.toString())
                    controller.getLength()
                    return true
                }
            }
            inputOuterD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && inputOuterD.text.isNotBlank()){
                    seekOut.progress = inputOuterD.text.toString().toInt()
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

    private fun setInputInnD(string: String?) {
        inputInnD.text = Editable.Factory.getInstance().newEditable(string)
    }
    private fun setInputOutD(string: String?) {
        inputOuterD.text = Editable.Factory.getInstance().newEditable(string)
    }

    override fun onDestroy() {
        super.onDestroy()
        (context as MainActivity).getRWatcher().watch(this)
    }
}
