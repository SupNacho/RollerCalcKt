package rck.supernacho.ru.rollercalckt.screens.preferences.view


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_preference.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentPreferenceBinding
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.preferences.domain.toImperialThickness
import rck.supernacho.ru.rollercalckt.screens.preferences.domain.toMetricThickness
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory
import java.math.BigDecimal

class PreferenceFragment : Fragment(), KodeinAware {

    private var saveAction: Runnable? = null
    override val kodein: Kodein by closestKodein()
    private lateinit var previousSystem: MeasureSystem
    private val viewModel: PrefsViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(PrefsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentPreferenceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_preference, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousSystem = viewModel.viewState.measureSystem
        initHints()
        initButtons()
        initSaveAction()
        initTextViews(view)
    }

    private fun initHints() {
        setHints(isMetric = viewModel.viewState.measureSystem == MeasureSystem.METRIC)
    }

    private fun initSaveAction() {
        saveAction = Runnable {
            viewModel.saveState()
        }
    }

    private fun initTextViews(view: View) {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                view.removeCallbacks(saveAction)
                view.postDelayed(saveAction, 1250)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }

        val isLimitsEnabled = viewModel.preferences.getSettings().isLimitsEnabled

        et_innerMax.run {
            addTextChangedListener(watcher)
            isEnabled = isLimitsEnabled
        }
        et_outerMax.run {
            addTextChangedListener(watcher)
            isEnabled = isLimitsEnabled
        }
    }

    private fun initButtons() {
        chip_Imperial.setOnClickListener {
            setHints(isMetric = false)
            viewModel.chooseMeasureSystem(MeasureSystem.IMPERIAL)
            setFieldsData(isMetric = false)
        }

        chip_Metric.setOnClickListener {
            setHints(isMetric = true)
            viewModel.chooseMeasureSystem(MeasureSystem.METRIC)
            setFieldsData(isMetric = true)
        }

        swt_weightEnabled.setOnClickListener {
            YandexMetrica.reportEvent("Weight switcher", "{\"IsEnabled\":\"${swt_weightEnabled.isChecked}\"")
            viewModel.enableWeightCalculation(swt_weightEnabled.isChecked)
        }

        swt_limitsEnabled.setOnClickListener {
            YandexMetrica.reportEvent("Limit switcher", "{\"IsEnabled\":\"${swt_limitsEnabled.isChecked}\"")
            viewModel.enableLimitCalculation(swt_limitsEnabled.isChecked)
            et_innerMax.isEnabled = swt_limitsEnabled.isChecked
            et_outerMax.isEnabled = swt_limitsEnabled.isChecked
        }
    }

    private fun setFieldsData(isMetric: Boolean) {
        val data = Pair(et_innerMax.text?.toString()?.toBigDecimal(), et_outerMax.text?.toString()?.toBigDecimal())
        setConvertedData(convertData(isMetric, data))
    }

    private fun convertData(isMetric: Boolean, data: Pair<BigDecimal?, BigDecimal?>): Pair<BigDecimal?, BigDecimal?> {
        YandexMetrica.reportEvent("Measure System", "{\"System\":\"${previousSystem.name}\"")
        return when {
            isMetric && previousSystem != MeasureSystem.METRIC -> {
                previousSystem = MeasureSystem.METRIC
                Pair(data.first?.toMetricThickness(), data.second?.toMetricThickness())
            }
            !isMetric && previousSystem != MeasureSystem.IMPERIAL -> {
                previousSystem = MeasureSystem.IMPERIAL
                Pair(data.first?.toImperialThickness(), data.second?.toImperialThickness())
            }
            else -> Pair(data.first, data.second)
        }
    }

    private fun setConvertedData(convertedData: Pair<BigDecimal?, BigDecimal?>) {
        et_innerMax.setText(convertedData.first.toString())
        et_outerMax.setText(convertedData.second.toString())
    }

    private fun setHints(isMetric: Boolean) {
        val hints = if (isMetric)
            Pair(R.string.settings_150_hint, R.string.settings_300_hint)
        else
            Pair(R.string.settings_150_hint_imp, R.string.settings_300_hint_imp)
        bindHints(hints)
    }

    private fun bindHints(hints: Pair<Int, Int>) {
        til_innerMax.hint = getString(hints.first)
        til_outerMax.hint = getString(hints.second)
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveState()
        view?.removeCallbacks(saveAction)
    }
}
