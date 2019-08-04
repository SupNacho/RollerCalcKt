package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.material.domain.CrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent

class EditMaterialViewModel(
        private val preferences: IPrefRepository,
        private val materials: IMaterialsRepository) : ViewModel() {
    private val interactor = CrudMaterialInteractor(preferences, materials)
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