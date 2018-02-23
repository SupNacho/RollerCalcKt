package rck.supernacho.ru.rollercalckt.controller

import android.widget.AdapterView
import android.widget.ArrayAdapter
import rck.supernacho.ru.rollercalckt.model.Material
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

object MainController {

    fun setAdapterCalcFragment(adapter: ArrayAdapter<Material>){
        adapterCalc = adapter
    }

    fun setAdapterAddFragment(adapter: ArrayAdapter<Material>){
        adapterAdd = adapter
    }

    fun setMaterialMapper(mapMat: MaterialMapper){
        materialMaper = mapMat
    }

    fun setMaterialController(matController: ManageableMaterials){
        materialController = matController
    }
    fun setCalcController(calController: Controllable){
        calcController = calController
    }

    fun getMaterialMapper(): MaterialMapper{
        return materialMaper
    }
    fun getMaterialList(): ArrayList<Material>{
        materials.clear()
        materials.addAll(materialMaper.getMaterials())
        return materials
    }

    fun updateLists(){
        adapterCalc.notifyDataSetChanged()
        adapterAdd.notifyDataSetChanged()
    }

    fun onCreate(){
        materialMaper.open()
    }

    fun onDestroy(){
        materialMaper.close()
    }

    private lateinit var calcController: Controllable
    private lateinit var materialController: ManageableMaterials
    private lateinit var materialMaper: MaterialMapper
    private val materials: ArrayList<Material> = ArrayList()
    private lateinit var adapterCalc: ArrayAdapter<Material>
    private lateinit var adapterAdd: ArrayAdapter<Material>
}