package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.Box
import io.objectbox.reactive.SubscriptionBuilder
import rck.supernacho.ru.rollercalckt.model.entity.Brand
import rck.supernacho.ru.rollercalckt.model.entity.Material

interface IBrandsRepository {
    val box: Box<Brand>
    val subscription: SubscriptionBuilder<Class<Brand>>
}