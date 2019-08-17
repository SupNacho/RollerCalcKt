package rck.supernacho.ru.rollercalckt.screens.material.view.adapter

import androidx.recyclerview.widget.DiffUtil
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

class MaterialDiffCallback: DiffUtil.ItemCallback<MaterialUi>() {
    override fun areItemsTheSame(oldItem: MaterialUi, newItem: MaterialUi): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: MaterialUi, newItem: MaterialUi): Boolean {
        return oldItem == newItem
    }
}