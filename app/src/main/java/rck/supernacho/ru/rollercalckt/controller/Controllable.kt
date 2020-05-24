package rck.supernacho.ru.rollercalckt.controller


interface Controllable {
    fun setOuterD(outD: Int)
    fun setInnerD(innD: Int)
    fun setThick(thick: Double)
    fun getLength()
}