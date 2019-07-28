package rck.supernacho.ru.rollercalckt.controller

import rck.supernacho.ru.rollercalckt.model.OldMaterial


interface ManageableMaterials {
    fun add(materialName: String, thickness: Double)
    fun remove(item: OldMaterial)
    fun edit(item: OldMaterial, materialName: String, thickness: Double)
}