package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_material.view.*

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.controller.CrudMaterialController
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.controller.ManageableMaterials
import rck.supernacho.ru.rollercalckt.model.OldMaterial
import java.lang.NumberFormatException


class EditMaterialFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener, IViewUpdate {

    private lateinit var editTextBrandName: EditText
    private lateinit var editTextBrandThick: EditText
    private lateinit var listViewMaterials: ListView
    private lateinit var buttonAdd: ImageButton
    private lateinit var buttonUpd: ImageButton
    private lateinit var buttonDel: ImageButton
    private lateinit var matController: ManageableMaterials
    private lateinit var adapter: ArrayAdapter<OldMaterial>
    private lateinit var materials: ArrayList<OldMaterial>
    private lateinit var selectedItem: OldMaterial

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_material, container, false)
        init(view)
        return view
    }

    private fun init(view: View){
        editTextBrandName = view.add_frag_edit_text_material_name
        editTextBrandThick = view.add_frag_edit_text_material_thick
        listViewMaterials = view.add_frag_list_view_materials
        buttonAdd = view.add_frag_button_new_material
        buttonUpd = view.add_frag_button_update
        buttonDel = view.add_frag_button_delete
        buttonAdd.setOnClickListener(this)
        buttonUpd.setOnClickListener(this)
        buttonDel.setOnClickListener(this)
        matController = CrudMaterialController()
        MainController.setMaterialController(matController)
        materials = MainController.getMaterialList()
        adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, materials)
        MainController.addUpdateListener(this)
        listViewMaterials.adapter = adapter
        listViewMaterials.onItemClickListener = this
        setButtonsState(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        MainController.removeUpdateListener(this)
    }

    override fun onItemClick(adapt: AdapterView<*>?, view: View?, pos: Int, l: Long) {
        val item = adapter.getItem(pos)
        if (item != null) {
            selectedItem = item
            editTextBrandName.text = Editable.Factory.getInstance().newEditable(selectedItem.brand)
            editTextBrandThick.text = Editable.Factory.getInstance().newEditable(selectedItem.thickness.toString())
            setButtonsState(true)
        }
    }

    override fun onClick(view: View?) {
        val materialName = editTextBrandName.text.toString()
        try {
            val materialThickness = editTextBrandThick.text.toString().toDouble()
            when (view) {
                buttonAdd -> {
                    if (materialName.isNotEmpty() && materialThickness.isFinite()) {
                        matController.add(materialName, materialThickness)
                        setButtonsState(false)
                        clearInput()
                    }
                }
                buttonUpd -> {
                    if (materialName.isNotEmpty() && materialThickness.isFinite()) {
                        matController.edit(selectedItem, materialName, materialThickness)
                        setButtonsState(false)
                        clearInput()
                    }
                }
                buttonDel -> {
                    matController.remove(selectedItem)
                    setButtonsState(false)
                    clearInput()
                }
                else -> {
                    Toast.makeText(context, "No such button", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: NumberFormatException){
            Toast.makeText(context, "Pls enter number in thickness field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInput(){
        editTextBrandName.text.clear()
        editTextBrandThick.text.clear()
    }

    override fun updateView() {
        adapter.notifyDataSetChanged()
    }

    private fun setButtonsState(udButtonsState: Boolean){
            if (udButtonsState) {
                buttonUpd.visibility = View.VISIBLE
                buttonDel.visibility = View.VISIBLE
            } else {
                buttonUpd.visibility = View.GONE
                buttonDel.visibility = View.GONE
            }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(command: String)
    }

    companion object {
        fun newInstance(): EditMaterialFragment {
            return EditMaterialFragment()
        }
    }
}
