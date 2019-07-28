package rck.supernacho.ru.rollercalckt.common.ui.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import rck.supernacho.ru.rollercalckt.BR

class DataBindingViewHolder<E, VM>(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: E, viewModel: VM){
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }
}