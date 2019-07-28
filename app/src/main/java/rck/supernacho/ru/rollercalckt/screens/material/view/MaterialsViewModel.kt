package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository

class MaterialsViewModel( private val preferences: IPrefRepository,  private val materials: IMaterialsRepository): ViewModel() {
    val materialsList : LiveData<List<Material>> by lazy {
       initMaterialLiveData()
    }

    private fun initMaterialLiveData(): LiveData<List<Material>>{
        val liveData = MutableLiveData<List<Material>>()
        liveData.value = materials.getRepo().all
        return liveData
    }
}