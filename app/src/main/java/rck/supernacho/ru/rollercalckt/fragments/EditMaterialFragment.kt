package rck.supernacho.ru.rollercalckt.fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.support.constraint.R.id.gone
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import rck.supernacho.ru.rollercalckt.MainActivity

import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.controller.CrudMaterialController
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.controller.ManageableMaterials
import rck.supernacho.ru.rollercalckt.model.Material


class EditMaterialFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener {

    private lateinit var editTextBrandName: EditText
    private lateinit var editTextBrandThick: EditText
    private lateinit var listViewMaterials: ListView
    private lateinit var buttonAdd: ImageButton
    private lateinit var buttonUpd: ImageButton
    private lateinit var buttonDel: ImageButton
    private lateinit var matController: ManageableMaterials
    private lateinit var adapter: ArrayAdapter<Material>
    private lateinit var materials: ArrayList<Material>
    private lateinit var selectedItem: Material

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_add_material, container, false)
        init(view)
        return view
    }

    private fun init(view: View){
        editTextBrandName = view.findViewById(R.id.add_frag_edit_text_material_name)
        editTextBrandThick = view.findViewById(R.id.add_frag_edit_text_material_thick)
        listViewMaterials = view.findViewById(R.id.add_frag_list_view_materials)
        buttonAdd = view.findViewById(R.id.add_frag_button_new_material)
        buttonUpd = view.findViewById(R.id.add_frag_button_update)
        buttonDel = view.findViewById(R.id.add_frag_button_delete)
        buttonAdd.setOnClickListener(this)
        buttonUpd.setOnClickListener(this)
        buttonDel.setOnClickListener(this)
        matController = CrudMaterialController(editTextBrandThick, editTextBrandName,
                listViewMaterials, buttonDel, buttonUpd, buttonAdd)
        MainController.setMaterialController(matController)
        materials = MainController.getMaterialList()
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, materials)
        MainController.setAdapterAddFragment(adapter)
        listViewMaterials.adapter = adapter
        listViewMaterials.onItemClickListener = this
        setButtonsState(false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        (context as MainActivity).getRWatcher().watch(this)
    }

    override fun onItemClick(adapt: AdapterView<*>?, view: View?, pos: Int, l: Long) {
        selectedItem = adapter.getItem(pos)
        editTextBrandName.text = Editable.Factory.getInstance().newEditable(selectedItem.brand)
        editTextBrandThick.text = Editable.Factory.getInstance().newEditable(selectedItem.thickness.toString())
        setButtonsState(true)
    }

    override fun onClick(view: View?) {
        when(view){
            buttonAdd -> {
                matController.add()
                setButtonsState(false)
            }
            buttonUpd -> {
                matController.edit(selectedItem)
                setButtonsState(false)
            }
            buttonDel -> {
                matController.remove(selectedItem)
                setButtonsState(false)
            }
            else -> {
                Toast.makeText(context, "No such button", Toast.LENGTH_SHORT).show()
            }
        }
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
