package rck.supernacho.ru.rollercalckt.model.calc

/**
 * Created by SuperNacho on 15.02.2018.
 */
interface Calculatable {
    fun getLength(dMax: Int, dMin: Int, thick: Double): String
    fun getThickness(nLayers: Int, rule: Thickness): Double
}