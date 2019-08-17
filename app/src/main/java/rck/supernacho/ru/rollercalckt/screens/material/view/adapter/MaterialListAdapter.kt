package rck.supernacho.ru.rollercalckt.screens.material.view.adapter

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.common.ui.adapter.DataBindingRVAdapter
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.material.view.MaterialsViewModel

class MaterialListAdapter(viewModel: MaterialsViewModel): DataBindingRVAdapter<MaterialUi, MaterialsViewModel>(MaterialDiffCallback(), viewModel) {
    override fun getItemViewType(position: Int): Int {
        return R.layout.material_item
    }
}