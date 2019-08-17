package rck.supernacho.ru.rollercalckt.model.helper.converter

import io.objectbox.converter.PropertyConverter
import java.lang.NumberFormatException
import java.math.BigDecimal

class BigDecimalToStringConverter : PropertyConverter<BigDecimal, String?> {
    override fun convertToDatabaseValue(entityProperty: BigDecimal?): String? = entityProperty?.toPlainString()

    override fun convertToEntityProperty(databaseValue: String?): BigDecimal {
        return databaseValue?.let {
            try {
                it.toBigDecimal()
            } catch (e: NumberFormatException){
                BigDecimal.ZERO
            }
        } ?: BigDecimal.ZERO
    }
}