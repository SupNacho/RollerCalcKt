package rck.supernacho.ru.rollercalckt.screens.preferences.domain

import com.digidemic.unitof.UnitOf
import java.math.BigDecimal

fun BigDecimal.toImperialWeight(): BigDecimal = UnitOf.Mass().fromKilograms(this.toDouble()).toPounds().toBigDecimal()
fun BigDecimal.toMetricWeight(): BigDecimal = UnitOf.Mass().fromPounds(this.toDouble()).toKilograms().toBigDecimal()
fun BigDecimal.toImperialLength(): BigDecimal = UnitOf.Length().fromMeters(this.toDouble()).toFeet().toBigDecimal()
fun BigDecimal.toMetricLength(): BigDecimal = UnitOf.Length().fromFeet(this.toDouble()).toMeters().toBigDecimal()
fun BigDecimal.toImperialThickness(): BigDecimal = UnitOf.Length().fromMillimeters(this.toDouble()).toInches().toBigDecimal()
fun BigDecimal.toMetricThickness(): BigDecimal = UnitOf.Length().fromInches(this.toDouble()).toMillimeters().toBigDecimal()
