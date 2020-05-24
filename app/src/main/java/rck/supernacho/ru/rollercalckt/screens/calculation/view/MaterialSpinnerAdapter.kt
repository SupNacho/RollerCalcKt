package rck.supernacho.ru.rollercalckt.screens.calculation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_material_item.view.*
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

class MaterialSpinnerAdapter(private val ctx: Context?): ArrayAdapter<MaterialUi>(ctx!!, R.layout.spinner_material_item) {
    var materials: List<MaterialUi> = emptyList()
    set(value) {
        super.clear()
        super.addAll(value)
        field = value
        notifyDataSetChanged()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getMaterialView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getMaterialView(position, parent)
    }

    private fun getMaterialView(position: Int, parent: ViewGroup): View{
        val view = LayoutInflater.from(ctx).inflate(R.layout.spinner_material_item, parent, false)
        val item = materials[position]
        view.run {
            tv_spinnerBrand.text = item.brand
            tv_spinnerName.text = item.name
            tv_spinnerThick.text = item.thickness
            tv_spinnerWeight.text = item.weight
            tv_spinnerDensity.text = item.weight
        }
        return view
    }
}