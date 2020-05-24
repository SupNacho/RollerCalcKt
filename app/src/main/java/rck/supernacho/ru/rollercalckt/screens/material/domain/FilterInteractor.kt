package rck.supernacho.ru.rollercalckt.screens.material.domain

import androidx.lifecycle.MutableLiveData
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

class FilterInteractor(private val filterableItems: MutableLiveData<List<MaterialUi>>) : IFilterMaterialInteractor {
    private var sortThickness: QueryConst? = null
    private var sortWeight: QueryConst? = null
    private var sortDensity: QueryConst? = null
    override var tempCollection: List<MaterialUi> = emptyList()


    override fun filterBy(input: CharSequence?) {
        input?.let {
            val result = tempCollection.filter { m ->
                m.brand?.contains(it, ignoreCase = true) == true
                        || m.name?.contains(it, ignoreCase = true) == true
                        || m.thickness?.contains(it, ignoreCase = true) == true
                        || m.weight?.contains(it, ignoreCase = true) == true
                        || m.density?.contains(it, ignoreCase = true) == true
            }
            filterableItems.postValue(result)
        } ?: filterableItems.postValue(tempCollection)
    }

    override fun sortByThickness() {
        val collection = filterableItems.value
        val result = when (sortThickness) {
            null, QueryConst.DESCENDING -> {
                sortThickness = QueryConst.ASCENDING
                collection?.sortedBy { m -> m.thickness?.toBigDecimal() }
            }
            QueryConst.ASCENDING -> {
                sortThickness = QueryConst.DESCENDING
                collection?.sortedByDescending { m -> m.thickness?.toBigDecimal() }
            }
        }
        filterableItems.postValue(result)
    }

    override fun sortByWeight() {
        val collection = filterableItems.value
        val result = when (sortWeight) {
            null, QueryConst.DESCENDING -> {
                sortWeight = QueryConst.ASCENDING
                collection?.sortedBy { m -> m.weight?.toBigDecimal() }
            }
            QueryConst.ASCENDING -> {
                sortWeight = QueryConst.DESCENDING
                collection?.sortedByDescending { m -> m.weight?.toBigDecimal() }
            }
        }
        filterableItems.postValue(result)
    }

    override fun sortByDensity() {
        val collection = filterableItems.value
        val result = when (sortDensity) {
            null, QueryConst.DESCENDING -> {
                sortDensity = QueryConst.ASCENDING
                collection?.sortedBy { m -> m.density?.toBigDecimal() }
            }
            QueryConst.ASCENDING -> {
                sortDensity = QueryConst.DESCENDING
                collection?.sortedByDescending { m -> m.density?.toBigDecimal() }
            }
        }
        filterableItems.postValue(result)
    }

    override fun sortByName() {
        val collection = filterableItems.value
        val result = when (sortDensity) {
            null, QueryConst.DESCENDING -> {
                sortDensity = QueryConst.ASCENDING
                collection?.sortedBy { m -> m.name }?.sortedBy { m -> m.brand }
            }
            QueryConst.ASCENDING -> {
                sortDensity = QueryConst.DESCENDING
                collection?.sortedByDescending { m -> m.name }?.sortedByDescending { m -> m.brand }
            }
        }
        filterableItems.postValue(result)
    }
}