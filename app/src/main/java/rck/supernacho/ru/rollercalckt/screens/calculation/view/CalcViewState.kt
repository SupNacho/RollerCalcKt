package rck.supernacho.ru.rollercalckt.screens.calculation.view

import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.preferences.view.PreferencesViewState
import java.math.BigDecimal

data class CalcViewState (
        val measureSystem: MeasureSystem,
        val innerInput: BigDecimal,
        val outerInput: BigDecimal,
        val innerInputMicrons: BigDecimal,
        val outerInputMicrons: BigDecimal,
        val thickness: BigDecimal,
        val weight: BigDecimal,
        val width: BigDecimal,
        val density: BigDecimal,
        val resultLength: BigDecimal,
        val resultWeight: BigDecimal,
        val preferencesViewState: PreferencesViewState
)