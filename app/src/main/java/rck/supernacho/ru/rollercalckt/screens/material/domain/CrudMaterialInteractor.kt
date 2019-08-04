package rck.supernacho.ru.rollercalckt.screens.material.domain

import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.entity.adapter.toMaterial
import rck.supernacho.ru.rollercalckt.model.entity.adapter.toUiModel
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository

class CrudMaterialInteractor(private val preferences: IPrefRepository,
                             private val materials: IMaterialsRepository) {

    fun getMaterials(): List<Material> {
        return emptyList()
    }

    fun getMaterial(id: Long): MaterialUi = materials.box.get(id).toUiModel()

    fun updateMaterial(materialUi: MaterialUi) {
        materials.box.put(materialUi.toMaterial())
    }

    fun addMaterial(materialUi: MaterialUi) {
        materials.box.put(materialUi.toMaterial())
    }
}