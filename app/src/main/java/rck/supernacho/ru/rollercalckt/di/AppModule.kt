package rck.supernacho.ru.rollercalckt.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.database.MaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.PreferecesRepository
import rck.supernacho.ru.rollercalckt.modelnew.helper.ObjectBox

val appModule = Kodein.Module("main_module", false){
    bind<IPrefRepository>() with singleton { PreferecesRepository() }
    bind<IMaterialsRepository>() with singleton { ObjectBox }
}