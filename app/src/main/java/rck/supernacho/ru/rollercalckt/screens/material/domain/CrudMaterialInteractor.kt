package rck.supernacho.ru.rollercalckt.screens.material.domain

import com.google.firebase.perf.FirebasePerformance
import io.objectbox.query.QueryBuilder
import io.objectbox.reactive.DataSubscription
import io.reactivex.Observable
import rck.supernacho.ru.rollercalckt.model.entity.*
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

    override fun getMaterials(order: QueryConst): List<MaterialUi> {
        val trace = FirebasePerformance.getInstance().newTrace("get Materials query")
        trace.start()
        val orderDirection = when(order){
            QueryConst.ASCENDING -> 0
            QueryConst.DESCENDING -> QueryBuilder.DESCENDING
        }
        val queryResult = materials.box.query().order(Material_.name, orderDirection).build().find()
        return when(order) {
            QueryConst.ASCENDING -> {
                trace.stop()
                queryResult.sortedBy { m -> m.brand.target.name }.map { material -> material.toUiModel() }
            }
            QueryConst.DESCENDING -> {
                trace.stop()
                queryResult.sortedByDescending { m -> m.brand.target.name }.map { material -> material.toUiModel() }
            }
        }
    }

    override fun getMaterial(id: Long): MaterialUi = materials.box.get(id).toUiModel()

    override fun getBrands(): List<BrandUi> {
        return brands.box.all.map { brand -> brand.toUiModel() }
    }

    override fun removeItem(materialUi: MaterialUi) {
        materials.box.remove(materialUi.toMaterial())
    }

    override fun updateMaterial(materialUi: MaterialUi) {
        val trace = FirebasePerformance.getInstance().newTrace("update material")
        trace.start()
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
        trace.stop()
    }

    override fun addMaterial(materialUi: MaterialUi) {
        getBrandId(materialUi)?.let { materialUi.brandId = it }
        materials.box.put(materialUi.toMaterial())
    }

    private fun getBrandId(materialUi: MaterialUi): Long? =
            materialUi.brand?.let { brands.box.query().equal(Brand_.name, it).build().findFirst()?.id }
}