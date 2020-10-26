package rck.supernacho.ru.rollercalckt.model.repository.database

import io.objectbox.Box
import io.objectbox.reactive.SubscriptionBuilder
import rck.supernacho.ru.rollercalckt.model.entity.Material

class MaterialsRepository(override val box: Box<Material>, override val subscription: SubscriptionBuilder<Class<Material>>): IMaterialsRepository