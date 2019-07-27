package rck.supernacho.ru.rollercalckt.modelnew.entity

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import rck.supernacho.ru.rollercalckt.modelnew.helper.converter.BigDecimalToStringConverter
import java.math.BigDecimal

@Entity
data class Material(
        @Id var id: Long = 0,
        @Convert(converter = BigDecimalToStringConverter::class, dbType = String::class)
        var thickness: BigDecimal = BigDecimal.ZERO,
        @Convert(converter = BigDecimalToStringConverter::class, dbType = String::class)
        var weight: BigDecimal = BigDecimal.ZERO,
        @Convert(converter = BigDecimalToStringConverter::class, dbType = String::class)
        var density: BigDecimal = BigDecimal.ZERO,
        @Convert(converter = BigDecimalToStringConverter::class, dbType = String::class)
        var mass: BigDecimal = BigDecimal.ZERO
) {
    lateinit var brand: ToOne<Brand>
}