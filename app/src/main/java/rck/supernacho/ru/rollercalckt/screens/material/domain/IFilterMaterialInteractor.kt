package rck.supernacho.ru.rollercalckt.screens.material.domain

import androidx.lifecycle.LiveData
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

interface IFilterMaterialInteractor {
    var tempCollection: List<MaterialUi>
    val filteredItems: LiveData<List<MaterialUi>>
    fun filterBy(input: CharSequence?)
    fun sortByThickness()
    fun sortByWeight()
    fun sortByDensity()
    fun sortByName()
}