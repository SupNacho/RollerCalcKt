package rck.supernacho.ru.rollercalckt.controller

import android.view.View
import android.widget.EditText
import android.widget.ListView
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.model.Material
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

class CrudMaterialController(vararg views: View) : ManageableMaterials{
    private val materialMapper : MaterialMapper = MainController.getMaterialMapper()
    private lateinit var editTextName : EditText
    private lateinit var editTextThick : EditText
    private lateinit var listView : ListView
    private val materials: ArrayList<Material> = MainController.getMaterialList()

    override fun add() {
        if (!editTextName.text.isNullOrEmpty() && !editTextThick.text.isNullOrEmpty()) {
            val id = materialMapper.insert(editTextName.text.toString(), editTextThick.text.toString().toDouble())
            if (id > -1) {
                materials.add(Material(id, editTextName.text.toString(), editTextThick.text.toString().toDouble()))
            }
            MainController.updateLists()
            clearInputFields()
        }
    }

    private fun clearInputFields() {
        editTextName.text.clear()
        editTextThick.text.clear()
    }

    override fun remove(item: Material) {
        materialMapper.delete(item)
        materials.remove(item)
        MainController.updateLists()
        clearInputFields()
    }

    override fun edit(item: Material) {
        materialMapper.update(item, editTextName.text.toString(),
                editTextThick.text.toString().toDouble())
        materials.clear()
        MainController.getMaterialList()
        MainController.updateLists()
        clearInputFields()
    }

    init {
        for (view in views){
            if (view is EditText && view.id == R.id.add_frag_edit_text_material_name) editTextName = view
            if (view is EditText && view.id == R.id.add_frag_edit_text_material_thick) editTextThick = view
            if (view is ListView && view.id == R.id.add_frag_list_view_materials) listView = view
        }
    }
}