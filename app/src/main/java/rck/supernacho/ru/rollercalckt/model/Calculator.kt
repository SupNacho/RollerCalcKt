package rck.supernacho.ru.rollercalckt.model

class Calculator {
    fun getLength(dMax: Int, dMin: Int, thick: Double): Double{
        val dRoll = (dMax - dMin) / 2
        val nLayers = dRoll / thick
        val middleLayer = (dMax + dMin) / 2
        return (middleLayer * Math.PI * nLayers) / 1000
    }

    fun getThickness(nLayers: Int, rule: Thickness): Double{
        when (rule){
            Thickness.TEN_MM ->{
                return 10.0 / nLayers
            }
            Thickness.ONE_MM -> return 1.0 / nLayers
        }
        return 0.0
    }
}