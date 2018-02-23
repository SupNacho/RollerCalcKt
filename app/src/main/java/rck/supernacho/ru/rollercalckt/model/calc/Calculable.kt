package rck.supernacho.ru.rollercalckt.model.calc

interface Calculable {
    fun getLength(dMax: Int, dMin: Int, thick: Double): String
    fun getThickness(nLayers: Int, rule: Thickness): Double
}