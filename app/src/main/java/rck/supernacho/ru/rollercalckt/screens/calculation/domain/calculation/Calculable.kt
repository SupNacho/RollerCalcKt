package rck.supernacho.ru.rollercalckt.screens.calculation.domain.calculation

import java.math.BigDecimal

interface Calculable {
    fun getLength(dMax: BigDecimal, dMin: BigDecimal, weight: BigDecimal): BigDecimal
    fun getWeight(length: BigDecimal, width: BigDecimal, thickness: BigDecimal,
                  weight: BigDecimal = BigDecimal.ZERO,
                  density: BigDecimal = BigDecimal.ZERO ): BigDecimal
}