package rck.supernacho.ru.rollercalckt.screens.material.view.event

import rck.supernacho.ru.rollercalckt.model.entity.Material

sealed class ClickEvent {
    data class EditClick(val material: Material): ClickEvent()
    object AddClick: ClickEvent()
    object DismissDialog: ClickEvent()
}