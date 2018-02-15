package rck.supernacho.ru.rollercalckt.model

/**
 * Created by SuperNacho on 15.02.2018.
 */
interface Calculatable {
    fun getLength(dMax: Int, dMin: Int, thick: Double): Double
    fun getThickness(nLayers: Int, rule: Thickness): Double
}