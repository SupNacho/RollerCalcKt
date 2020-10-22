package rck.supernacho.ru.rollercalckt.screens.calculation.view.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.selector_dialog.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.setOnRightDrawableClick
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class SelectMaterialDialog: BottomSheetDialogFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModel: SelectorViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(SelectorViewModel::class.java)
    }

    private val adapter = SelectorRVAdapter { materialUi ->
        (parentFragment as? OnMaterialSelected)?.onSelected(materialUi)
        dismissAllowingStateLoss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.selector_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_materials.adapter = adapter
        viewModel.materialData.observe(viewLifecycleOwner) {
            adapter.materials = it
        }

        btn_filterByName.setOnClickListener { viewModel.sortByName() }
        btn_filterByThick.setOnClickListener { viewModel.sortByThick() }
        btn_filterByWeight.setOnClickListener { viewModel.sortByWeight() }
        btn_filterByDensity.setOnClickListener { viewModel.sortByDensity() }

        et_searchMaterial.run {
            doOnTextChanged { text, _, _, _ ->
                viewModel.filterByText(text.toString())
            }

            setOnRightDrawableClick { et_searchMaterial.text?.clear() }
        }
    }

    interface OnMaterialSelected{
        fun onSelected(item: MaterialUi)
    }
}