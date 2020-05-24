package rck.supernacho.ru.rollercalckt.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import rck.supernacho.ru.rollercalckt.model.entity.Brand
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.PreferecesRepository
import rck.supernacho.ru.rollercalckt.model.helper.ObjectBox
import rck.supernacho.ru.rollercalckt.model.repository.database.BrandsRepository
import rck.supernacho.ru.rollercalckt.model.repository.database.IBrandsRepository
import rck.supernacho.ru.rollercalckt.model.repository.database.MaterialsRepository
import rck.supernacho.ru.rollercalckt.screens.calculation.domain.calculation.Calculable
import rck.supernacho.ru.rollercalckt.screens.calculation.domain.calculation.Calculator
import rck.supernacho.ru.rollercalckt.screens.material.domain.CrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor

val appModule = Kodein.Module("main_module", false){
    bind<IPrefRepository>() with singleton { PreferecesRepository(instance()) }
    bind<IMaterialsRepository>() with singleton { MaterialsRepository(ObjectBox.getRepository(Material::class.java), ObjectBox.subscribe(Material::class.java)) }
    bind<IBrandsRepository>() with singleton { BrandsRepository(ObjectBox.getRepository(Brand::class.java), ObjectBox.subscribe(Brand::class.java)) }
    bind<ICrudMaterialInteractor>() with provider { CrudMaterialInteractor(preferences = instance(), materials = instance(), brands = instance()) }
    bind<Calculable>() with provider { Calculator() }
}