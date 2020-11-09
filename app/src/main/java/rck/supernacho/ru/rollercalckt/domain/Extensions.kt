package rck.supernacho.ru.rollercalckt.domain

import java.math.BigDecimal

fun BigDecimal.isZero() =
        this.compareTo(BigDecimal.ZERO) == 0

fun String?.toBigDecimalOrDef(default: Double = 0.0): BigDecimal {
    val digitString = this?.onlyDigit()
    return if (digitString.isNullOrEmpty())
        BigDecimal(default)
    else
        digitString.toBigDecimal()
}

fun String.onlyDigit() = this.replace("""[^\d\\.]""".toRegex(), "")