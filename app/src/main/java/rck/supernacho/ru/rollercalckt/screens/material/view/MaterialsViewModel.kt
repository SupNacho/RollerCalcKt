package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.schedulers.Schedulers
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.material.domain.FilterInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.IFilterMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import timber.log.Timber

class MaterialsViewModel(private val interactor: ICrudMaterialInteractor) : ViewModel() {
    val materialsList: LiveData<List<MaterialUi>> by lazy {
        initMaterialLiveData()
    }
    private val clickState = LiveEvent<ClickEvent>()
    val actionState: LiveData<ClickEvent> = clickState
    val filterInteractor: IFilterMaterialInteractor = FilterInteractor(materialsList as MutableLiveData)
    private val dataSubscription = interactor.dataSubscription
            .subscribeOn(Schedulers.io())
            .subscribe { data ->
                Timber.d("Updates received: $data")
                val updatedData = interactor.getMaterials()
                (materialsList as MutableLiveData<List<MaterialUi>>).postValue(updatedData)
                filterInteractor.tempCollection = updatedData
            }

    private fun initMaterialLiveData(): LiveData<List<MaterialUi>> {
        val liveData = MutableLiveData<List<MaterialUi>>()
        liveData.postValue(interactor.getMaterials())
        return liveData
    }

    fun onClickDeleteItem(materialUi: MaterialUi) {
        Timber.d("Delete clicked")
        interactor.removeItem(materialUi)
    }

    fun onClickEdit(material: MaterialUi) {
        Timber.d("Edit clicked")
        clickState.value = ClickEvent.EditClick(material)
    }

    fun onClickAdd() {
        Timber.d("Add clicked")
        clickState.value = ClickEvent.AddClick
    }

    override fun onCleared() {
        super.onCleared()
        dataSubscription.dispose()
    }
}