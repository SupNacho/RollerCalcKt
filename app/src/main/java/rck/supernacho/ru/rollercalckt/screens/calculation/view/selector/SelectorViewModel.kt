package rck.supernacho.ru.rollercalckt.screens.calculation.view.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.material.domain.CrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.FilterInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.IFilterMaterialInteractor

class SelectorViewModel(
        private val filterInteractor: IFilterMaterialInteractor,
        private val materialInteractor: ICrudMaterialInteractor
) : ViewModel() {
    private val _materialsData = MutableLiveData<List<MaterialUi>>()
    private val disposables = CompositeDisposable()


    val materialData: LiveData<List<MaterialUi>> = filterInteractor.filteredItems


    init {
        disposables.add(
                materialInteractor.dataSubscription
                        .subscribeOn(Schedulers.io())
                        .subscribe {
                            val data = materialInteractor.getMaterials()
                            _materialsData.postValue(data)
                            filterInteractor.tempCollection = data
                        }
        )
    }

    fun sortByName() = filterInteractor.sortByName()
    fun sortByThick() = filterInteractor.sortByThickness()
    fun sortByWeight() = filterInteractor.sortByWeight()
    fun sortByDensity() = filterInteractor.sortByDensity()

    fun filterByText(text: String) = filterInteractor.filterBy(text)


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}