package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import java.math.BigDecimal

class EditMaterialViewModel(
        private val preferences: IPrefRepository,
        private val materials: IMaterialsRepository) : ViewModel() {

    private val clickState = LiveEvent<ClickEvent>()
    val actionState: LiveData<ClickEvent> = clickState
    val brandField = ObservableField<String>()
    val nameField = ObservableField<String>()
    val thicknessField = ObservableField<BigDecimal>()
    val weightField = ObservableField<BigDecimal>()
    val densityField = ObservableField<BigDecimal>()
    var materialId: Long? = null
        set(value) {
            field = value
            value?.let {
                materials.box.get(it).let { mat ->
                    brandField.set(mat.brand.target.name)
                    nameField.set(mat.name)
                    thicknessField.set(mat.thickness)
                    weightField.set(mat.thickness)
                    densityField.set(mat.density)
                }
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

        closeDialog()
    }

    private fun addMaterial() {

        closeDialog()
    }

    private fun closeDialog() {
        clickState.value = ClickEvent.DismissDialog
    }
}