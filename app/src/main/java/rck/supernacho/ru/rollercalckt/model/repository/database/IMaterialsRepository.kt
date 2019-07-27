package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.BoxStore

interface IMaterialsRepository {
    fun getRepository(): BoxStore
}