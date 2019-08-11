package rck.supernacho.ru.rollercalckt.screens.material.domain

import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

interface IFilterMaterialInteractor {
    var tempCollection: List<MaterialUi>
    fun filterBy(input: CharSequence?)
    fun sortByThickness()
    fun sortByWeight()
    fun sortByDensity()
    fun sortByName()
}