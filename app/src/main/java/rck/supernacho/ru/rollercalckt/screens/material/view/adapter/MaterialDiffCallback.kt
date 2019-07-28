package rck.supernacho.ru.rollercalckt.screens.material.view.adapter

import androidx.recyclerview.widget.DiffUtil
import rck.supernacho.ru.rollercalckt.model.entity.Material

class MaterialDiffCallback: DiffUtil.ItemCallback<Material>() {
    override fun areItemsTheSame(oldItem: Material, newItem: Material): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Material, newItem: Material): Boolean {
        return oldItem == newItem
    }
}