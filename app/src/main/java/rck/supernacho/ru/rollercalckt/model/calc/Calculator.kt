package rck.supernacho.ru.rollercalckt.model.calc

class Calculator: Calculatable {
    override fun getLength(dMax: Int, dMin: Int, thick: Double): String {
        val dRoll = (dMax - dMin) / 2
        val nLayers = dRoll / thick
        val middleLayer = (dMax + dMin) / 2
        return String.format("%.2f",(middleLayer * Math.PI * nLayers) / 1000)
    }

    override fun getThickness(nLayers: Int, rule: Thickness): Double{
        when (rule){
            Thickness.TEN_MM ->{
                return 10.0 / nLayers
            }
            Thickness.ONE_MM -> return 1.0 / nLayers
        }
    }
}