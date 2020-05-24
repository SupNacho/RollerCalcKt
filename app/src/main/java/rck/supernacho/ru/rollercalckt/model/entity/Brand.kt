package rck.supernacho.ru.rollercalckt.model.entity

import io.objectbox.annotation.*
import io.objectbox.relation.ToMany

@Entity
data class Brand (
        @Id var id: Long = 0,
        @Unique
        var name: String? = null
) {
    @Backlink(to = "brand")
    lateinit var materials: ToMany<Material>
}