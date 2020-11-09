package rck.supernacho.ru.rollercalckt.screens.calculation.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignLeft
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_calculation.*
import kotlinx.android.synthetic.main.spinner_material_item.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentCalculationBinding
import rck.supernacho.ru.rollercalckt.domain.toBigDecimalOrDef
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.calculation.view.selector.SelectMaterialDialog
import rck.supernacho.ru.rollercalckt.screens.custom.setVisibility
import rck.supernacho.ru.rollercalckt.screens.preferences.domain.toImperialThickness
import rck.supernacho.ru.rollercalckt.screens.setBalloonSettings
import rck.supernacho.ru.rollercalckt.screens.utils.BalloonType
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory
import java.math.BigDecimal

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
                viewLifecycleOwner, {
            it.find { it.id == viewModel.viewState.value?.selectedMaterial }
                    ?.let { onSelected(it) }
                    ?: it.firstOrNull()?.let { onSelected(it) }
        }
        )
        viewModel.viewState.observe(viewLifecycleOwner, { renderUi(it) })
        initSelector()
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
            YandexMetrica.reportEvent("Balloon Showed", "{\"Balloon type\":\"${BalloonType.THICK.name}\"")
            val thickBalloon = createBalloon(requireContext()) { setBalloonSettings(v, viewLifecycleOwner, requireContext().getString(R.string.thick_balloon, pcs, oneMicron, pcs, pcs)) }
            v.showAlignLeft(thickBalloon)
        }
        tv_spinnerWeight.setOnClickListener { v ->
            YandexMetrica.reportEvent("Balloon Showed", "{\"Balloon type\":\"${BalloonType.WEIGHT.name}\"")
            val weightBalloon = createBalloon(requireContext()) { setBalloonSettings(v, viewLifecycleOwner, requireContext().getString(R.string.weight_balloon)) }
            v.showAlignLeft(weightBalloon)
        }
        tv_spinnerDensity.setOnClickListener { v ->
            YandexMetrica.reportEvent("Balloon Showed", "{\"Balloon type\":\"${BalloonType.DENSITY.name}\"")
            val densityBalloon = createBalloon(requireContext()) { setBalloonSettings(v, viewLifecycleOwner, requireContext().getString(R.string.density_balloon)) }
            v.showAlignLeft(densityBalloon)
        }
    }

    private fun initSelector() {
        fl_materialItemRoot.setOnClickListener {
            SelectMaterialDialog.getInstance(viewModel.viewState.value?.preferencesViewState?.isWeightCalculate ?: false).show(childFragmentManager, SelectMaterialDialog::class.java.name)
        }

        viewModel.viewState.value?.preferencesViewState?.isWeightCalculate?.let {
            tv_spinnerDensity.setVisibility(isVisible = it)
            tv_spinnerWeight.setVisibility(isVisible = it)
        }
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
        val isLimited = viewModel.viewState.value?.preferencesViewState?.isLimitsEnabled ?: false
        val innerLimit = viewModel.viewState.value?.preferencesViewState?.limits?.inner.toBigDecimalOrDef(100.0)
        val outerLimit = viewModel.viewState.value?.preferencesViewState?.limits?.outer.toBigDecimalOrDef(300.0)
        ivet_inner.setOnChangeListener { input ->
            val inner = when {
                isLimited && (input.toBigDecimalOrNull()
                        ?: BigDecimal.ZERO) > innerLimit -> innerLimit.toPlainString()
                else -> input
            }
            viewModel.setInput(inner, true)
            inner
        }


        ivet_outer.setOnChangeListener{ input ->
            val outer = when {
                isLimited && (input.toBigDecimalOrNull()
                        ?: BigDecimal.ZERO) > outerLimit -> outerLimit.toPlainString()
                else -> input
            }
            viewModel.setInput(outer, false)
            outer
        }

        ivet_width.setOnChangeListener { input ->
            viewModel.setWidth(input)
            input
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
            ivet_width.run {
                hint = getString(R.string.calc_material_width, unitString)
                visibility = weightVisibility
            }
            ivet_inner.hint = getString(R.string.calc_inner_d_text_view, unitString)
            ivet_outer.hint = getString(R.string.calc_outer_d_text_view, unitString)
            cv_result.setData(
                    length = getString(R.string.calc_output, resultLength, lUnitString),
                    weight = getString(R.string.calc_output_weight, resultWeight, wUnitString),
                    yard = getString(R.string.calc_output_yard, yard),
                    isYardEnabled = true,
                    isWeightEnabled = preferencesViewState.isWeightCalculate
            )

            if (isInitScreen)
                preferencesViewState.run {
                    isInitScreen = false
                    val (inner, outer, width) = when (measureSystem) {
                        MeasureSystem.METRIC -> Triple(lastInput.inner, lastInput.outer, lastInput.width)
                        MeasureSystem.IMPERIAL -> Triple(
                                lastInput.inner?.toBigDecimalOrNull()?.toImperialThickness(),
                                lastInput.outer?.toBigDecimalOrNull()?.toImperialThickness(),
                                lastInput.width?.toBigDecimalOrNull()?.toImperialThickness()
                        )
                    }
                    ivet_inner.text = inner.toString()
                    ivet_outer.text = outer.toString()
                    ivet_width.text = width.toString()
                }

        }
    }
}
