package rck.supernacho.ru.rollercalckt.screens.preferences.view


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_preference.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentPreferenceBinding
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class PreferenceFragment : Fragment(), KodeinAware {

    private var firstLaunch = true
    private var saveAction: Runnable? = null
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
        chip_Imperial.setOnClickListener { viewModel.chooseMeasureSystem(MeasureSystem.IMPERIAL) }
        chip_Metric.setOnClickListener { viewModel.chooseMeasureSystem(MeasureSystem.METRIC) }
        saveAction = Runnable {
            viewModel.saveState()
            showSavedPopUp()
        }
        val watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                view.removeCallbacks(saveAction)
                view.postDelayed(saveAction, 1250)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }
        et_innerMax.addTextChangedListener(watcher)
        et_outerMax.addTextChangedListener(watcher)
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveState()
        view?.removeCallbacks(saveAction)
    }

    private fun showSavedPopUp() {
        if (firstLaunch) {
            firstLaunch = false
        } else {
            status_popup.show()
        }
    }
}
