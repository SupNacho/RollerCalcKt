package rck.supernacho.ru.rollercalckt.screens.calculation.view


import android.content.Intent
import android.database.DataSetObserver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_calculation.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.MainActivity
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentCalculationBinding
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class CalculationFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModel: CalculationViewModel by lazy {
        ViewModelProviders.of(this, RCViewModelFactory(kodein)).get(CalculationViewModel::class.java)
    }

    private var spinnerAdapter: MaterialSpinnerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentCalculationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculation, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_oldCalc.setOnClickListener { startActivity(Intent(context, MainActivity::class.java)) }
        spinnerAdapter = MaterialSpinnerAdapter(context)
        sp_materials.adapter = spinnerAdapter
        sp_materials.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                sp_materials.setSelection(0)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                viewModel.onSelectedMaterial(position)
            }
        }
        viewModel.materialsList.observe(this, Observer { spinnerAdapter?.materials = it })
        viewModel.viewState.observe(this, Observer { renderUi(it) })
        btn_filter.setOnClickListener {
            fl_filter.run {
                visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
        et_inner.addTextChangedListener {
            viewModel.setInput(it.toString(), true)
        }
        et_outer.addTextChangedListener {
            viewModel.setInput(it.toString(), false)
        }
        et_width.addTextChangedListener {
            viewModel.setWidth(it.toString())
        }

    }

    private fun renderUi(vs: CalcViewState) {
        vs.run {
            val unit = if (measureSystem == MeasureSystem.METRIC)R.string.calc_measure_metric else R.string.calc_measure_imperial
            val wUnit = if (measureSystem == MeasureSystem.METRIC)R.string.calc_w_measure_metric else R.string.calc_w_measure_imperial
            val lUnit = if (measureSystem == MeasureSystem.METRIC)R.string.calc_l_measure_metric else R.string.calc_l_measure_imperial
            val unitString = getString(unit)
            val wUnitString = getString(wUnit)
            val lUnitString = getString(lUnit)
            til_width.hint = getString(R.string.calc_material_width, unitString)
            til_inner.hint = getString(R.string.calc_inner_d_text_view, unitString)
            til_outer.hint = getString(R.string.calc_outer_d_text_view, unitString)
            tv_output.text = getString(R.string.calc_output, resultLength)
            tv_outputDesc.text = getString(R.string.calc_output_length, lUnitString)
            tv_outputWeight.text = getString(R.string.calc_output_weight, resultWeight, wUnitString)
        }
    }
}
