package rck.supernacho.ru.rollercalckt.controller

import rck.supernacho.ru.rollercalckt.model.Material


interface ManageableMaterials {
    fun add()
    fun remove(item: Material)
    fun edit(item: Material)
}