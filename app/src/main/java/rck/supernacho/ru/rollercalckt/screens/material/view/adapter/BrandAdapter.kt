package rck.supernacho.ru.rollercalckt.screens.material.view.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import kotlinx.android.synthetic.main.custom_row.view.*
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi

class BrandAdapter(context: Context, private val items: List<BrandUi>): ArrayAdapter<BrandUi>(context, R.layout.custom_row, items) {

    private val tempList: MutableList<BrandUi> = ArrayList(items)
    private val suggestList: MutableList<BrandUi> = ArrayList()
    private val filter = object: Filter(){
        override fun convertResultToString(resultValue: Any?): CharSequence? {
            return (resultValue as BrandUi).name
        }

        override fun performFiltering(input: CharSequence?): FilterResults {
            input?.let {
                suggestList.clear()
                val filtered = tempList.filter { b ->  b.name?.toLowerCase()?.startsWith(it.toString().toLowerCase()) == true}
                suggestList.addAll(filtered)

                return FilterResults().apply {
                    values = suggestList
                    count = suggestList.size
                }
            } ?: return FilterResults()
        }

        override fun publishResults(input: CharSequence?, results: FilterResults?) {
            val tempVal = results?.values as? List<BrandUi>
            if ( tempVal != null && tempVal.isNotEmpty()){
                clear()
                tempVal.forEach { b -> add(b) }
            } else {
                clear()
            }
            notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: (context as Activity).layoutInflater.inflate(R.layout.custom_row, parent, false)
        view.tv_itemName.text = getItem(position)?.name
        return view
    }

    override fun getItem(position: Int): BrandUi? {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun getFilter(): Filter {
        return filter
    }


}