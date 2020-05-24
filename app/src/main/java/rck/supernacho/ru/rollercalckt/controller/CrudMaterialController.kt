package rck.supernacho.ru.rollercalckt.controller

import rck.supernacho.ru.rollercalckt.model.OldMaterial
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

class CrudMaterialController : ManageableMaterials{
    private val materialMapper : MaterialMapper = MainController.getMaterialMapper()
    private val materials: ArrayList<OldMaterial> = MainController.getMaterialList()

    override fun add(materialName: String, thickness: Double) {

            val id = materialMapper.insert(materialName, thickness)
            if (id > -1) {
                materials.add(OldMaterial(id, materialName, thickness))

            MainController.updateLists()
        }
    }

    override fun remove(item: OldMaterial) {
        materialMapper.delete(item)
        materials.remove(item)
        MainController.updateLists()
    }

    override fun edit(item: OldMaterial, materialName: String, thickness: Double) {
        materialMapper.update(item, materialName, thickness)
        materials.clear()
        MainController.getMaterialList()
        MainController.updateLists()
    }
}