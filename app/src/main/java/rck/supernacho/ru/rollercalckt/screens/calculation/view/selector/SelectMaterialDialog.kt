package rck.supernacho.ru.rollercalckt.screens.calculation.view.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.selector_dialog.rv_materials
import kotlinx.android.synthetic.main.selector_dialog.sv_sortMaterial
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class SelectMaterialDialog : BottomSheetDialogFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModel: SelectorViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(SelectorViewModel::class.java)
    }

    private val adapter: SelectorRVAdapter by lazy {
        SelectorRVAdapter(arguments?.getBoolean(IS_WEIGHT) ?: false) { materialUi ->
            (parentFragment as? OnMaterialSelected)?.onSelected(materialUi)
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.selector_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_materials.adapter = adapter
        viewModel.materialData.observe(viewLifecycleOwner) {
            adapter.materials = it
        }

        sv_sortMaterial.run {
            isWeightEnabled = arguments?.getBoolean(IS_WEIGHT) ?: false
            setOnChangeListener { viewModel.filterByText(it) }
            setNameSortAction { viewModel.sortByName() }
            setThickSortAction { viewModel.sortByThick() }
            setWeightSortAction { viewModel.sortByWeight() }
            setDensitySortAction { viewModel.sortByDensity() }
        }
    }

    companion object {
        private const val IS_WEIGHT = "is_weight"
        fun getInstance(isWeightEnabled: Boolean): BottomSheetDialogFragment {
            return SelectMaterialDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_WEIGHT, isWeightEnabled)
                }
            }
        }
    }

    interface OnMaterialSelected {
        fun onSelected(item: MaterialUi)
    }
}