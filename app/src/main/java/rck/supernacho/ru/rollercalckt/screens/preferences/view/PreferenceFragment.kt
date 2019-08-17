package rck.supernacho.ru.rollercalckt.screens.preferences.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_preference.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentPreferenceBinding
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.calculation.view.CalculationViewModel
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class PreferenceFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModel: PrefsViewModel by lazy {
        ViewModelProviders.of(this, RCViewModelFactory(kodein)).get(PrefsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentPreferenceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_preference, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chip_Imperial.setOnClickListener { viewModel.chooiseMeasureSystem(MeasureSystem.IMPERIAL) }
        chip_Metric.setOnClickListener { viewModel.chooiseMeasureSystem(MeasureSystem.METRIC) }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveState()
    }
}
