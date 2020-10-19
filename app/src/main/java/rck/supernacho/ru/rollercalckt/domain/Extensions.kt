package rck.supernacho.ru.rollercalckt.domain

import java.math.BigDecimal

fun BigDecimal.isZero() =
        this.compareTo(BigDecimal.ZERO) == 0