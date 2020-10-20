package rck.supernacho.ru.rollercalckt.screens.material.view

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.edit_material_dialog.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.EditMaterialDialogBinding
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.material.view.adapter.BrandAdapter
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class EditMaterialDialog : BottomSheetDialogFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()

    private val viewModel: EditMaterialViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(EditMaterialViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: EditMaterialDialogBinding = DataBindingUtil.inflate(inflater, R.layout.edit_material_dialog, container, false)
        binding.viewModel = viewModel
        arguments?.let {
            viewModel.materialId = it.getLong(MATERIAL_ID)
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        binding.material = viewModel.materialUi
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickEventsObserver()
        initButton()
        context?.let {
            initBrandAdapter(it)
            initFiledsWithHints(it)
        }

        (dialog as BottomSheetDialog).behavior.run {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
    }

    private fun initClickEventsObserver() {
        viewModel.actionState.observe(this, Observer {
            when (it) {
                is ClickEvent.DismissDialog -> dismiss()
                else -> Unit
            }
        })
    }

    private fun initButton() {
        btn_positiveActionEditDialog.text =
                if (arguments?.getLong(MATERIAL_ID) != null) getString(R.string.edit)
                else getString(R.string.add)
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

    private fun initFiledsWithHints(it: Context) {
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

    companion object {

        private const val MATERIAL_ID = "material_id"

        fun getInstance(materialId: Long? = null): DialogFragment {
            val instance = EditMaterialDialog()
            materialId?.let {
                instance.arguments = Bundle().apply {
                    putLong(MATERIAL_ID, it)
                }
            }
            return instance
        }
    }
}