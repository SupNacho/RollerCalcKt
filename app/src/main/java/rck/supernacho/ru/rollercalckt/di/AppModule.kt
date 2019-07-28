package rck.supernacho.ru.rollercalckt.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.PreferecesRepository
import rck.supernacho.ru.rollercalckt.model.helper.ObjectBox
import rck.supernacho.ru.rollercalckt.model.repository.database.MaterialsRepository

val appModule = Kodein.Module("main_module", false){
    bind<IPrefRepository>() with singleton { PreferecesRepository() }
    bind<IMaterialsRepository>() with singleton { MaterialsRepository(ObjectBox.getRepository(Material::class.java)) }
}