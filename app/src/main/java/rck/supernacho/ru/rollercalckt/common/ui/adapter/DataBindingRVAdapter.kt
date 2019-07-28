package rck.supernacho.ru.rollercalckt.common.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import rck.supernacho.ru.rollercalckt.common.ui.viewholder.DataBindingViewHolder

abstract class DataBindingRVAdapter<T, VM>(diffCallback: DiffUtil.ItemCallback<T>, private val viewModel: VM): ListAdapter<T, DataBindingViewHolder<T, VM>>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T, VM> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T, VM>, position: Int) = holder.bind(getItem(position), viewModel)
}