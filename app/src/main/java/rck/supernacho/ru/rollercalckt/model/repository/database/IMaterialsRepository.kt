package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.Box
import rck.supernacho.ru.rollercalckt.model.entity.Material

interface IMaterialsRepository {
    fun getRepo(): Box<Material>
}