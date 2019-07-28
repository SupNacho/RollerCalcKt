package rck.supernacho.ru.rollercalckt.common.ui.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import rck.supernacho.ru.rollercalckt.BR

class DataBindingViewHolder<T>(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T){
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}