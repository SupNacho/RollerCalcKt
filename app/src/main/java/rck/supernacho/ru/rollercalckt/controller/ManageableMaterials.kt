package rck.supernacho.ru.rollercalckt.controller

import rck.supernacho.ru.rollercalckt.model.Material


interface ManageableMaterials {
    fun add(materialName: String, thickness: Double)
    fun remove(item: Material)
    fun edit(item: Material, materialName: String, thickness: Double)
}