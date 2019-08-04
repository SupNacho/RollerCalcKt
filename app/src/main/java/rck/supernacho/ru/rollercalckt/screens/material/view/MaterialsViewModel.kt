package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import timber.log.Timber

class MaterialsViewModel(private val preferences: IPrefRepository, private val materials: IMaterialsRepository) : ViewModel() {
    val materialsList: LiveData<List<Material>> by lazy {
        initMaterialLiveData()
    }
    private val clickState = LiveEvent<ClickEvent>()
    val actionState: LiveData<ClickEvent> = clickState

    private val dataSubscription = materials.subscription
            .onError { Timber.e(it) }
            .observer { data ->
                Timber.d("Updates received: $data")
                (materialsList as MutableLiveData<List<Material>>).postValue(materials.box.all)
            }

    private fun initMaterialLiveData(): LiveData<List<Material>> {
        val liveData = MutableLiveData<List<Material>>()
        liveData.postValue(materials.box.all)
        return liveData
    }

    fun onClickDeleteItem(material: Material) {
        Timber.d("Delete clicked")
        materials.box.remove(material)
    }

    fun onClickEdit(material: Material){
        Timber.d("Edit clicked")
        clickState.value = ClickEvent.EditClick(material)
    }

    fun onClickAdd(){
        Timber.d("Add clicked")
        clickState.value = ClickEvent.AddClick
    }

    override fun onCleared() {
        super.onCleared()
        dataSubscription.cancel()
    }
}