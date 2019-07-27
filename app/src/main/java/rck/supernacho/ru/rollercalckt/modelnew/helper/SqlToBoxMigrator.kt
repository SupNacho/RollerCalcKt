package rck.supernacho.ru.rollercalckt.modelnew.helper

import android.content.Context
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import rck.supernacho.ru.rollercalckt.model.MaterialMapper
import rck.supernacho.ru.rollercalckt.model.database.DataBaseFields
import rck.supernacho.ru.rollercalckt.model.database.DataBaseHelper
import rck.supernacho.ru.rollercalckt.modelnew.entity.Brand
import rck.supernacho.ru.rollercalckt.modelnew.entity.Material
import timber.log.Timber
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

typealias OldMaterial = rck.supernacho.ru.rollercalckt.model.Material

object SqlToBoxMigrator : CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val brandBox: Box<Brand> = ObjectBox.getRepository().boxFor()
    private val materialBox: Box<Material> = ObjectBox.getRepository().boxFor()

    fun checkAndMigrate(context: Context): Boolean {
        return runBlocking {
            with(Dispatchers.IO) {
                if (!context.databaseList().contains(DataBaseFields.DATABASE_NAME.field))
                    return@runBlocking true
                
                val oldDB = MaterialMapper(context)
                oldDB.open()
                val result = oldDB.getMaterials()
                return@runBlocking if (result.isEmpty())
                    with(Dispatchers.Main){
                        true
                    }
                else {
                    try {
                        brandBox.removeAll()
                        materialBox.removeAll()
                        result.forEach { m -> mapMaterial(m) }
                        with(Dispatchers.Main){
                            true
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                        with(Dispatchers.Main){
                            false
                        }
                    } finally {
                        oldDB.close()
                    }
                }
            }
        }
    }


    private fun mapMaterial(material: OldMaterial) {
        val newBrand = Brand(name = material.brand)
        val newMaterial = Material(thickness = material.thickness.toBigDecimal())
        Timber.d("THREAD ${Thread.currentThread().name}")
        newMaterial.brand.target = newBrand
        materialBox.put(newMaterial)
    }
}