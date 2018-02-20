package rck.supernacho.ru.rollercalckt.controller

import android.content.Context
import android.util.Log
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
        materialMapper = MainData.getMaterialMapper()//MaterialMapper(context)
        materials = MainData.getMaterialList()
//        materialMapper.open()
        for (view in views){
            if (view is EditText && view.id == R.id.add_frag_edit_text_material_name) editTextName = view
            if (view is EditText && view.id == R.id.add_frag_edit_text_material_thick) editTextThick = view
            if (view is ListView && view.id == R.id.add_frag_list_view_materials) listView = view
        }
    }

    override fun add() {
        val id = materialMapper.insert(editTextName.text.toString(), editTextThick.text.toString().toDouble())
        materials.add(Material(id, editTextName.text.toString(), editTextThick.text.toString().toDouble()))
        Log.d("++", "CRUD" + materials.hashCode())
    }

    override fun remove() {
        materialMapper.delete(listView.selectedItem as Material)
    }

    override fun edit() {
        materialMapper.update(listView.selectedItem as Material, editTextName.text.toString(),
                editTextThick.text.toString().toDouble())
    }
}