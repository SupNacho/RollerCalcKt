package rck.supernacho.ru.rollercalckt.screens.preferences.view

import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.model.entity.UserInput

data class PreferencesViewState(
        var limits: UserInput,
        var measureSystem: MeasureSystem,
        var lastInput: UserInput,
        var innerLimitsDescription: String = "",
        var outerLimitsDescription: String = ""
)