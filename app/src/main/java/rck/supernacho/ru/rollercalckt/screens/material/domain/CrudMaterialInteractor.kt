package rck.supernacho.ru.rollercalckt.screens.material.domain

import io.objectbox.reactive.DataSubscription
import io.reactivex.Observable
import rck.supernacho.ru.rollercalckt.model.entity.Brand
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.Brand_
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.entity.adapter.toMaterial
import rck.supernacho.ru.rollercalckt.model.entity.adapter.toUiModel
import rck.supernacho.ru.rollercalckt.model.repository.database.IBrandsRepository
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository

class CrudMaterialInteractor(private val preferences: IPrefRepository,
                             private val materials: IMaterialsRepository,
                             private val brands: IBrandsRepository) : ICrudMaterialInteractor {

    private var subscriptionMaterial: DataSubscription? = null
    override val dataSubscription : Observable<Boolean> = Observable.create { emit ->
        subscriptionMaterial = materials.subscription.observer { emit.onNext(true) }
    }

    override fun getMaterials(): List<MaterialUi> {
        return materials.box.all.map { material -> material.toUiModel() }
    }

    override fun getMaterial(id: Long): MaterialUi = materials.box.get(id).toUiModel()

    override fun getBrands(): List<BrandUi> {
        return brands.box.all.map { brand -> brand.toUiModel() }
    }

    override fun removeItem(materialUi: MaterialUi) {
        materials.box.remove(materialUi.toMaterial())
    }

    override fun updateMaterial(materialUi: MaterialUi) {
        materialUi.brandId?.let {
            val oldBrand = brands.box.get(it)
            if (materialUi.brand != oldBrand.name) {
                val existId =  getBrandId(materialUi)
                if (existId != null)
                    materialUi.brandId = existId
                else {
                    materialUi.brandId = materialUi.brand?.let { name -> brands.box.put(Brand(name = name)) }
                }
            }
        }
        materials.box.put(materialUi.toMaterial())
    }

    override fun addMaterial(materialUi: MaterialUi) {
        getBrandId(materialUi)?.let { materialUi.brandId = it }
        materials.box.put(materialUi.toMaterial())
    }

    private fun getBrandId(materialUi: MaterialUi): Long? =
            materialUi.brand?.let { brands.box.query().equal(Brand_.name, it).build().findFirst()?.id }

}