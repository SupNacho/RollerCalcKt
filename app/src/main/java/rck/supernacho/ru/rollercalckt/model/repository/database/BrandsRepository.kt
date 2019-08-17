package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.Box
import io.objectbox.reactive.SubscriptionBuilder
import rck.supernacho.ru.rollercalckt.model.entity.Brand
import rck.supernacho.ru.rollercalckt.model.entity.Material

class BrandsRepository(override val box: Box<Brand>, override val subscription: SubscriptionBuilder<Class<Brand>>): IBrandsRepository