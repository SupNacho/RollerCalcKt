package rck.supernacho.ru.rollercalckt

import org.junit.Test

import org.junit.Assert.*
import rck.supernacho.ru.rollercalckt.model.calc.Calculator
import rck.supernacho.ru.rollercalckt.model.calc.Thickness

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val calc = Calculator()

    @Test
    fun calc_isCorrect(){
        assertEquals(50,calc.getLength(144,84,0.212).toInt())
    }

    @Test
    fun calc_thickness_isCorrect(){
        assertEquals(0.2, calc.getThickness(5, Thickness.ONE_MM), 0.09)
        assertEquals(0.5, calc.getThickness(20, Thickness.TEN_MM), 0.09)
    }


}
