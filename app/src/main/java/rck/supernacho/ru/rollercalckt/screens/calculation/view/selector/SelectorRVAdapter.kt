package rck.supernacho.ru.rollercalckt.screens.calculation.view.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.spinner_material_item.view.*
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

class SelectorRVAdapter(private val onClick: (MaterialUi) ->  Unit): RecyclerView.Adapter<SelectorRVAdapter.SelectorMaterialViewHolder>() {
    var materials: List<MaterialUi> = emptyList()
    set(value) {
        val diffUtil = DiffUtil.calculateDiff(SelectorDiffUtilCallback(field, value))
        field = value
        diffUtil.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorMaterialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_material_item, parent, false)
        return SelectorMaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectorMaterialViewHolder, position: Int) {
        holder.bind(materials[position])
    }

    override fun getItemCount(): Int = materials.size

    inner class SelectorMaterialViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: MaterialUi) {
            containerView.run {
                setOnClickListener { onClick(item) }
                tv_spinnerBrand.text = item.brand
                tv_spinnerName.text = item.name
                tv_spinnerThick.text = item.thickness
                tv_spinnerWeight.text = item.weight
                tv_spinnerDensity.text = item.density
            }
        }
    }
}