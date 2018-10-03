package rck.supernacho.ru.rollercalckt.controller

import rck.supernacho.ru.rollercalckt.fragments.IViewUpdate
import rck.supernacho.ru.rollercalckt.model.Material
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

object MainController {

    fun addUpdateListener(updateListener: IViewUpdate){
        updateList.add(updateListener)
    }

    fun setMaterialMapper(mapMat: MaterialMapper){
        materialMapper = mapMat
    }

    fun setMaterialController(matController: ManageableMaterials){
        materialController = matController
    }
    fun setCalcController(calController: Controllable){
        calcController = calController
    }

    fun getMaterialMapper(): MaterialMapper{
        return materialMapper
    }
    fun getMaterialList(): ArrayList<Material>{
        if(!materialMapper.isOpened()) materialMapper.open()
            materials.clear()
            materials.addAll(materialMapper.getMaterials())

        return materials
    }

    fun updateLists(){
        updateList.forEach{ e -> e.updateView()}
    }

    fun onCreate(){
        materialMapper.open()
    }

    fun onDestroy(){
        materialMapper.close()
    }

    private lateinit var calcController: Controllable
    private lateinit var materialController: ManageableMaterials
    private lateinit var materialMapper: MaterialMapper
    private val materials: ArrayList<Material> = ArrayList()
    private val updateList = ArrayList<IViewUpdate>()
}