package rck.supernacho.ru.rollercalckt.screens.calculation.view


import android.content.Intent
import android.database.DataSetObserver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
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
        viewModel.materialsList.observe(this, Observer { spinnerAdapter?.materials = it })
        btn_filter.setOnClickListener {
            fl_filter.run {
                visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }
    }
}
