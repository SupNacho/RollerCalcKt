package rck.supernacho.ru.rollercalckt.screens.calculation.domain.calculation

import java.math.BigDecimal
import java.math.RoundingMode


class Calculator : Calculable {
    override fun getLength(dMax: BigDecimal, dMin: BigDecimal, thick: BigDecimal): BigDecimal {
        val dRoll = (dMax - dMin) / BigDecimal(2)
        val nLayers = if (thick == BigDecimal.ZERO) BigDecimal.ZERO else dRoll / thick
        val middleLayer = (dMax + dMin) / BigDecimal(2)
        return ((middleLayer * Math.PI.toBigDecimal() * nLayers) / BigDecimal(1000))
                .setScale(5, RoundingMode.HALF_UP)
    }

    override fun getWeight(length: BigDecimal, width: BigDecimal, thickness: BigDecimal, weight: BigDecimal, density: BigDecimal): BigDecimal {
        val s = length.multiply(width.divide(BigDecimal(1000)))
        val result = when {
            weight > BigDecimal.ZERO -> s.multiply(weight)
            density > BigDecimal.ZERO -> s.multiply(thickness).multiply(density)
            else -> BigDecimal.ZERO
        }
        return result.setScale(2, RoundingMode.HALF_UP)
    }
}