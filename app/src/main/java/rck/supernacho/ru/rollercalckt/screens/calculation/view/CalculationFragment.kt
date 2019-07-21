package rck.supernacho.ru.rollercalckt.screens.calculation.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentCalculationBinding
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class CalculationFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModel: CalculationViewModel by lazy {
        ViewModelProviders.of(this, RCViewModelFactory(kodein)).get(CalculationViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentCalculationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculation, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

}
