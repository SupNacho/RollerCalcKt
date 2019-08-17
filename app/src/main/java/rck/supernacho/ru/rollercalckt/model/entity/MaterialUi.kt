package rck.supernacho.ru.rollercalckt.model.entity

data class MaterialUi(
        var id: Long = 0,
        var name: String? = null,
        var thickness: String? = null,
        var weight: String? = null,
        var density: String? = null,
        var mass: String? = null,
        var brand: String? = null,
        var brandId: Long? =null
)