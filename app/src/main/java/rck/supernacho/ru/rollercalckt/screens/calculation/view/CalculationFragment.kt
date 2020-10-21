package rck.supernacho.ru.rollercalckt.screens.calculation.view


import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect
import kotlinx.android.synthetic.main.fragment_calculation.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.spinner_material_item.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.MainActivity
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentCalculationBinding
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.calculation.view.selector.SelectMaterialDialog
import rck.supernacho.ru.rollercalckt.screens.setBalloonSettings
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class CalculationFragment : Fragment(), KodeinAware, SelectMaterialDialog.OnMaterialSelected {

    override val kodein: Kodein by closestKodein()
    private val viewModel: CalculationViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(CalculationViewModel::class.java)
    }
    private var isInitScreen = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentCalculationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculation, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.materialsList.observe(
                viewLifecycleOwner, Observer {
            it.find { it.id == viewModel.viewState.value?.selectedMaterial }
                    ?.let { onSelected(it) }
                    ?: it.firstOrNull()?.let { onSelected(it) }
        }
        )
        viewModel.viewState.observe(viewLifecycleOwner, Observer { renderUi(it) })
        initSelector()
        initButtons()
        initInputViews()
        initBalloonHints()
    }

    private fun initBalloonHints() {
        val (pcs, oneMicron) = if (viewModel.viewState.value?.preferencesViewState?.measureSystem == MeasureSystem.IMPERIAL)
            Pair(
                    requireContext().getString(R.string.calc_measure_imperial),
                    requireContext().getString(R.string.one_micron_imp)
            )
        else
            Pair(
                    requireContext().getString(R.string.calc_measure_metric),
                    requireContext().getString(R.string.one_micron_m)
            )


        tv_spinnerThick.setOnClickListener { v ->
            val thickBalloon = createBalloon(requireContext()) { setBalloonSettings(v, viewLifecycleOwner, requireContext().getString(R.string.thick_balloon, pcs, oneMicron, pcs, pcs)) }
            v.showAlignLeft(thickBalloon)
        }
        tv_spinnerWeight.setOnClickListener { v ->
            val weightBalloon = createBalloon(requireContext()) { setBalloonSettings(v, viewLifecycleOwner, requireContext().getString(R.string.weight_balloon)) }
            v.showAlignLeft(weightBalloon)
        }
        tv_spinnerDensity.setOnClickListener { v ->
            val densityBalloon = createBalloon(requireContext()) { setBalloonSettings(v, viewLifecycleOwner, requireContext().getString(R.string.density_balloon)) }
            v.showAlignLeft(densityBalloon)
        }
    }

    private fun initSelector() {
        fl_materialItemRoot.setOnClickListener {
            SelectMaterialDialog().show(childFragmentManager, SelectMaterialDialog::class.java.name)
        }
    }

    private fun initButtons() {
        btn_oldCalc.setOnClickListener { startActivity(Intent(context, MainActivity::class.java)) }
    }

    override fun onSelected(item: MaterialUi) {
        viewModel.onSelectedMaterial(item)
        setMaterial(item)
    }

    private fun setMaterial(item: MaterialUi) {
        tv_spinnerBrand.text = item.brand
        tv_spinnerName.text = item.name
        tv_spinnerThick.text = item.thickness
        tv_spinnerWeight.text = item.weight
        tv_spinnerDensity.text = item.density
    }

    private fun initInputViews() {
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
            val weightVisibility = if (preferencesViewState.isWeightCalculate) View.VISIBLE else View.GONE
            val unit = if (measureSystem == MeasureSystem.METRIC) R.string.calc_measure_metric else R.string.calc_measure_imperial
            val wUnit = if (measureSystem == MeasureSystem.METRIC) R.string.calc_w_measure_metric else R.string.calc_w_measure_imperial
            val lUnit = if (measureSystem == MeasureSystem.METRIC) R.string.calc_l_measure_metric else R.string.calc_l_measure_imperial
            val unitString = getString(unit)
            val wUnitString = getString(wUnit)
            val lUnitString = getString(lUnit)
            til_width.run {
                hint = getString(R.string.calc_material_width, unitString)
                visibility = weightVisibility
            }
            til_inner.hint = getString(R.string.calc_inner_d_text_view, unitString)
            til_outer.hint = getString(R.string.calc_outer_d_text_view, unitString)
            tv_output.text = getString(R.string.calc_output, resultLength)
            tv_outputDesc.text = getString(R.string.calc_output_length, lUnitString)
            tv_outputWeight.run {
                text = getString(R.string.calc_output_weight, resultWeight, wUnitString)
                visibility = weightVisibility
            }

            if (isInitScreen)
                preferencesViewState.run {
                    isInitScreen = false
                    et_inner.setText(lastInput.inner)
                    et_outer.setText(lastInput.outer)
                    et_width.setText(lastInput.width)
                }

        }
    }
}
