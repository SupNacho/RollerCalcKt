package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.Box
import io.objectbox.reactive.SubscriptionBuilder
import rck.supernacho.ru.rollercalckt.model.entity.Material

interface IMaterialsRepository {
    val box: Box<Material>
    val subscription: SubscriptionBuilder<Class<Material>>
}