package rck.supernacho.ru.rollercalckt.controller

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ListView
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.Material
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

class CrudMaterialController : ManageableMaterials{
    private val viewsArr : Array<out View>
    private val materialMapper : MaterialMapper
    private lateinit var editTextName : EditText
    private lateinit var editTextThick : EditText
    private lateinit var listView : ListView
    private val materials: ArrayList<Material>

    constructor(context: Context, vararg views: View){
        viewsArr = views
        materialMapper = MainData.getMaterialMapper()
        materials = MainData.getMaterialList()
        for (view in views){
            if (view is EditText && view.id == R.id.add_frag_edit_text_material_name) editTextName = view
            if (view is EditText && view.id == R.id.add_frag_edit_text_material_thick) editTextThick = view
            if (view is ListView && view.id == R.id.add_frag_list_view_materials) listView = view
        }
    }

    override fun add() {
        if (!editTextName.text.isNullOrEmpty() && !editTextThick.text.isNullOrEmpty()) {
            val id = materialMapper.insert(editTextName.text.toString(), editTextThick.text.toString().toDouble())
            materials.add(Material(id, editTextName.text.toString(), editTextThick.text.toString().toDouble()))
        }
    }

    override fun remove(item: Material) {
        materialMapper.delete(item)
        materials.remove(item)
    }

    override fun edit(item: Material) {
        materialMapper.update(item, editTextName.text.toString(),
                editTextThick.text.toString().toDouble())
        materials.clear()
        MainData.getMaterialList()
    }
}