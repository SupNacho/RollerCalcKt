package rck.supernacho.ru.rollercalckt.screens.material.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.edit_material_dialog.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.EditMaterialDialogBinding
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.hideKeyboard
import rck.supernacho.ru.rollercalckt.screens.material.view.adapter.BrandAdapter
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory


class ManageMaterialFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val args: ManageMaterialFragmentArgs by navArgs()

    private val viewModel: ManageMaterialViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(ManageMaterialViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: EditMaterialDialogBinding = DataBindingUtil.inflate(inflater, R.layout.edit_material_dialog, container, false)
        binding.viewModel = viewModel
        if (args.materialId > -1)
            viewModel.materialId = args.materialId

        binding.material = viewModel.materialUi
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickEventsObserver()
        initButton()
        context?.let {
            initBrandAdapter(it)
            initFieldsWithHints(it)
        }
    }

    private fun initClickEventsObserver() {
        viewModel.actionState.observe(viewLifecycleOwner, {
            when (it) {
                is ClickEvent.DismissDialog -> findNavController().navigateUp()
                else -> Unit
            }
        })
    }

    private fun initButton() {
        val actionString = if (args.materialId > -1)
            getString(R.string.edit)
        else
            getString(R.string.add)

        tb_manageMaterial.run {
            title = actionString
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        
        btn_positiveActionEditDialog.text = actionString

    }

    private fun initBrandAdapter(it: Context) {
        val adapter = BrandAdapter(it, viewModel.brandsList)
        actv_brandEditDialog.threshold = 1
        actv_brandEditDialog.setAdapter(adapter)
        actv_brandEditDialog.setOnItemClickListener { adapterView, _, i, _ ->
            val brand = adapterView.getItemAtPosition(i) as BrandUi
            actv_brandEditDialog.text?.let { et ->
                et.replace(0, et.length, brand.name)
            }
        }
    }

    private fun initFieldsWithHints(it: Context) {
        val (thickness, weight, density) = if (viewModel.preferences.measureSystem == MeasureSystem.METRIC)
            Triple(R.string.calc_measure_metric, R.string.weight_pcs_metric, R.string.density_pcs)
        else
            Triple(R.string.calc_measure_imperial, R.string.weight_pcs_metric, R.string.density_pcs)

        val isWeightEnabled = if (viewModel.preferences.isWeightCalculate) View.VISIBLE else View.GONE

        til_thicknessDialog.hint = it.getString(R.string.thickness, it.getString(thickness))
        til_weightDialog.run {
            hint = it.getString(R.string.weight, it.getString(weight))
            visibility = isWeightEnabled
        }
        til_densityDialog.run {
            hint = it.getString(R.string.density, it.getString(density))
            visibility = isWeightEnabled
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

}