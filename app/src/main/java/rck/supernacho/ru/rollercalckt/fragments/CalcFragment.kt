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
import android.widget.*

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.controller.CalcController
import rck.supernacho.ru.rollercalckt.controller.Controllable

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CalcFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CalcFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalcFragment : Fragment(), View.OnKeyListener, View.OnClickListener, View.OnFocusChangeListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var cont: Context? = null
    private lateinit var resultTextView: TextView
    private lateinit var inputOuterD: EditText
    private lateinit var inputInnD: EditText
    private lateinit var seekIn: SeekBar
    private lateinit var seekOut: SeekBar
    private lateinit var spinner: Spinner
    private lateinit var controller: Controllable

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
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_calc, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        resultTextView = view.findViewById(R.id.calc_fragment_text_view_output)
        spinner = view.findViewById(R.id.calc_fragment_spinner_material)
        seekIn = view.findViewById(R.id.calc_fragment_seek_inner_d)
        seekIn.max = 300
        seekOut = view.findViewById(R.id.calc_fragment_seek_outer_d)
        seekOut.max = 1000
        inputOuterD = view.findViewById(R.id.calc_fragment_outer_d)
        inputOuterD.text = Editable.Factory.getInstance().newEditable("678")
        inputInnD = view.findViewById(R.id.calc_fragment_inner_d)
        inputInnD.text = Editable.Factory.getInstance().newEditable("123")
        inputInnD.setOnKeyListener(this)
        inputInnD.setOnFocusChangeListener(this)
        inputOuterD.setOnKeyListener(this)
        inputOuterD.setOnFocusChangeListener(this)
        controller = CalcController(context, inputInnD, inputOuterD, resultTextView)

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalcFragment.
         */
        // TODO: Rename and change types and number of parameters
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
                    Log.d("--", "Inner "+inputInnD.text.length)
                    seekIn.progress = inputInnD.text.toString().toInt()
                    tempInnD = inputInnD.text.toString()
                    controller.getLength()
                    return true
                }
            }
            inputOuterD -> {
                if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER
                        && inputOuterD.text.isNotBlank()){
                    Log.d("--", "Outer "+inputOuterD.text.length)
                    seekOut.progress = inputOuterD.text.toString().toInt()
                    tempOutD = inputOuterD.text.toString()
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

    override fun onClick(p0: View?) {

    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when(p0){
            inputInnD -> {
                if (p1) {
                    Log.d("--", "OnClick innreD")
                    tempInnD = inputInnD.text.toString()
                    inputInnD.text = Editable.Factory.getInstance().newEditable("")
                } else {
                    inputInnD.text = Editable.Factory.getInstance().newEditable(tempInnD)
                }
            }
            inputOuterD -> {
                if (p1) {
                    Log.d("--", "OnClick outerD")
                    tempOutD = inputOuterD.text.toString()
                    inputOuterD.text = Editable.Factory.getInstance().newEditable("")
                } else {
                    inputOuterD.text = Editable.Factory.getInstance().newEditable(tempOutD)
                }
            }
        }
    }
}
