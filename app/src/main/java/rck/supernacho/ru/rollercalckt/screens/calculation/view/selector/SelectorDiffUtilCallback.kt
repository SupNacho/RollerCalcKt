package rck.supernacho.ru.rollercalckt.screens.calculation.view.selector

import androidx.recyclerview.widget.DiffUtil
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

class SelectorDiffUtilCallback(
        private val oldList: List<MaterialUi>,
        private val newList: List<MaterialUi>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
}