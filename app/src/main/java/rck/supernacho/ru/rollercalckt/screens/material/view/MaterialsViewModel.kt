package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import timber.log.Timber

class MaterialsViewModel( private val preferences: IPrefRepository,  private val materials: IMaterialsRepository): ViewModel() {
    val materialsList : LiveData<List<Material>> by lazy {
       initMaterialLiveData()
    }
    private val dataSubscription = materials.subscription.observer { data ->
        Timber.d("Updates received: $data")
        (materialsList as MutableLiveData<List<Material>>).value = materials.box.all
    }

    private fun initMaterialLiveData(): LiveData<List<Material>>{
        val liveData = MutableLiveData<List<Material>>()
        liveData.value = materials.box.all
        return liveData
    }

    fun onClickDeleteItem(id: Long){
        materials.box.remove(id)
    }

    override fun onCleared() {
        super.onCleared()
        dataSubscription.cancel()
    }
}