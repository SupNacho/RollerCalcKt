package rck.supernacho.ru.rollercalckt.modelnew.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Brand (
        @Id var id: Long = 0,
        var name: String? = null
) {
    @Backlink(to = "brand")
    lateinit var materials: ToMany<Material>
}