package rck.supernacho.ru.rollercalckt.screens.material.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_calculation.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.MainActivity
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentMaterialBinding
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory


class MaterialFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModel: MaterialsViewModel by lazy {
        ViewModelProviders.of(this, RCViewModelFactory(kodein)).get(MaterialsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentMaterialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_oldCalc.setOnClickListener { startActivity(Intent(context, MainActivity::class.java)) }
    }
}
