package rck.supernacho.ru.rollercalckt.screens.material.view

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.schedulers.Schedulers
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.material.domain.FilterInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.IFilterMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import rck.supernacho.ru.rollercalckt.screens.utils.BalloonType
import timber.log.Timber

class MaterialsViewModel(
        private val interactor: ICrudMaterialInteractor,
        private val filterInteractor: IFilterMaterialInteractor,
        val preferences: IPrefRepository
) : ViewModel() {
    val materialsList: LiveData<List<MaterialUi>> by lazy { filterInteractor.filteredItems }
    private val clickState = LiveEvent<ClickEvent>()
    val actionState: LiveData<ClickEvent> = clickState

    private val dataSubscription = interactor.dataSubscription
            .subscribeOn(Schedulers.io())
            .subscribe { data ->
                Timber.d("Updates received: $data")
                val updatedData = interactor.getMaterials()
                (materialsList as MutableLiveData<List<MaterialUi>>).postValue(updatedData)
                filterInteractor.tempCollection = updatedData
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

    fun onClickShowSort() {
        clickState.value = ClickEvent.ShowSortClick
    }

    fun onClickSelect(material: MaterialUi) {
        val prefViewState = preferences.getSettings()
        preferences.saveSettings(prefViewState.copy(lastInput = prefViewState.lastInput.copy(lastSelectedMaterial = material.id)))
        clickState.value = ClickEvent.SelectClick(material)
    }

    fun onClickBalloon(view: View, type: BalloonType) {
        clickState.value = ClickEvent.BalloonClick(view, type)
    }

    fun sortByName() = filterInteractor.sortByName()
    fun sortByThick() = filterInteractor.sortByThickness()
    fun sortByWeight() = filterInteractor.sortByWeight()
    fun sortByDensity() = filterInteractor.sortByDensity()

    fun filterByText(text: String) = filterInteractor.filterBy(text)

    override fun onCleared() {
        super.onCleared()
        dataSubscription.dispose()
    }
}