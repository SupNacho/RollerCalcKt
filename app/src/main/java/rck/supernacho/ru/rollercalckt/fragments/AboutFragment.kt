package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rck.supernacho.ru.rollercalckt.MainActivity

import rck.supernacho.ru.rollercalckt.R

import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.tv_about_policy.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://supnacho.github.io/rollercalc/privacy_policy.html")))
        }
        return view
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
        (context as MainActivity).getRWatcher().watch(this)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(command: String)
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}
