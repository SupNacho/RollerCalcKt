package rck.supernacho.ru.rollercalckt.model

class Material(val id: Long, val brand: String, val thickness: Double) {
    override fun toString(): String {
        return "@ $brand  s: ${thickness}mm"
    }
}