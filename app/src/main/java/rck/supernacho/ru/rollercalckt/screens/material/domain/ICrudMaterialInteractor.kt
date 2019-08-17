package rck.supernacho.ru.rollercalckt.screens.material.domain

import io.reactivex.Observable
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

interface ICrudMaterialInteractor {

    val dataSubscription : Observable<Boolean>

    fun getMaterials(order: QueryConst  = QueryConst.ASCENDING): List<MaterialUi>

    fun getBrands(): List<BrandUi>

    fun getMaterial(id: Long): MaterialUi

    fun updateMaterial(materialUi: MaterialUi)

    fun addMaterial(materialUi: MaterialUi)

    fun removeItem(materialUi: MaterialUi)
}