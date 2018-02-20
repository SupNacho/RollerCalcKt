package rck.supernacho.ru.rollercalckt.controller

import rck.supernacho.ru.rollercalckt.model.Material
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

object MainData {

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

    fun onResume(){
        materialMaper.open()
    }

    fun onDestroy(){
        materialMaper.close()
    }

    private lateinit var calcController: Controllable
    private lateinit var materialController: ManageableMaterials
    private lateinit var materialMaper: MaterialMapper
    private val materials: ArrayList<Material> = ArrayList()
}