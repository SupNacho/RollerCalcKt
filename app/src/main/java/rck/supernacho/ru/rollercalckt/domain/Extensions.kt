package rck.supernacho.ru.rollercalckt.domain

import java.math.BigDecimal

fun BigDecimal.isZero() =
        this.compareTo(BigDecimal.ZERO) == 0

fun String?.toBigDecimalOrDef(default: Double = 0.0): BigDecimal {
    return if (this.isNullOrEmpty())
        BigDecimal(default)
    else
        this.toBigDecimal()
}