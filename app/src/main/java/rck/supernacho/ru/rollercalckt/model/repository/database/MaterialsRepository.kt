package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.Box
import rck.supernacho.ru.rollercalckt.model.entity.Material

class MaterialsRepository(private val box: Box<Material>): IMaterialsRepository {
    override fun getRepo(): Box<Material> {
        return box
    }
}