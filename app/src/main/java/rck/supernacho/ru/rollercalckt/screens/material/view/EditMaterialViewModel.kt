package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent

class EditMaterialViewModel(private val interactor: ICrudMaterialInteractor) : ViewModel() {
    private val clickState = LiveEvent<ClickEvent>()
    val actionState: LiveData<ClickEvent> = clickState
    var materialUi: MaterialUi? = MaterialUi()
        private set
    var materialId: Long? = null
        set(value) {
            field = value
            value?.let {
                materialUi = interactor.getMaterial(it)
            }
        }

    val brandsList: List<BrandUi> = interactor.getBrands()

    fun positiveAction() {
        if (materialId != null) updateMaterial()
        else addMaterial()
    }

    fun negativeAction() {
        closeDialog()
    }

    private fun updateMaterial() {
        materialUi?.let { interactor.updateMaterial(it) }
        closeDialog()
    }

    private fun addMaterial() {
        materialUi?.let { interactor.addMaterial(it) }
        closeDialog()
    }

    private fun closeDialog() {
        clickState.value = ClickEvent.DismissDialog
    }
}