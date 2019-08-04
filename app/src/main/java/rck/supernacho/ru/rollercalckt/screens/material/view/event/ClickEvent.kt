package rck.supernacho.ru.rollercalckt.screens.material.view.event

import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

sealed class ClickEvent {
    data class EditClick(val material: MaterialUi): ClickEvent()
    object AddClick: ClickEvent()
    object DismissDialog: ClickEvent()
}