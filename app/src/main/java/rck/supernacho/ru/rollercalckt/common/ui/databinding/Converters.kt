package rck.supernacho.ru.rollercalckt.common.ui.databinding

import androidx.databinding.BindingConversion
import java.math.BigDecimal

@BindingConversion
fun bigDecimalToString(value: BigDecimal?): String? = value?.toPlainString() ?: BigDecimal.ZERO.toPlainString()