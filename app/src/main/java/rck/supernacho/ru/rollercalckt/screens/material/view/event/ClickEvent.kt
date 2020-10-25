package rck.supernacho.ru.rollercalckt.screens.material.view.event

import android.view.View
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.screens.utils.BalloonType

sealed class ClickEvent {
    data class EditClick(val material: MaterialUi): ClickEvent()
    data class SelectClick(val material: MaterialUi): ClickEvent()
    data class BalloonClick(val view: View, val type: BalloonType): ClickEvent()
    object AddClick: ClickEvent()
    object ShowSortClick: ClickEvent()
    object DismissDialog: ClickEvent()
}