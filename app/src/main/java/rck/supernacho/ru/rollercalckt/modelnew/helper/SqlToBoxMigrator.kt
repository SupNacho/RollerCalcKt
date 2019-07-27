package rck.supernacho.ru.rollercalckt.modelnew.helper

import android.content.Context
import com.yandex.metrica.YandexMetrica
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import rck.supernacho.ru.rollercalckt.model.MaterialMapper
import rck.supernacho.ru.rollercalckt.model.database.DataBaseFields
import rck.supernacho.ru.rollercalckt.modelnew.entity.Brand
import rck.supernacho.ru.rollercalckt.modelnew.entity.Material
import timber.log.Timber

typealias OldMaterial = rck.supernacho.ru.rollercalckt.model.Material

object SqlToBoxMigrator : CoroutineScope by CoroutineScope(SupervisorJob()) {
    private val materialBox: Box<Material> = ObjectBox.getRepository().boxFor()

    fun checkAndMigrateAsync(context: Context) {
        Timber.d("THREAD ${Thread.currentThread().name}")
        if (!context.databaseList().contains(DataBaseFields.DATABASE_NAME.field))
            return
        val oldDB = MaterialMapper(context)
        oldDB.open()
        val result = oldDB.getMaterials()
        if (result.isEmpty()) {
            removeOldDB(context, result)
        } else {
            try {
                result.forEach { m -> mapMaterial(m) }
                removeOldDB(context, result)
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                oldDB.close()
            }
        }
    }

    private fun removeOldDB(context: Context, result: ArrayList<rck.supernacho.ru.rollercalckt.model.Material>) {
        context.deleteDatabase(DataBaseFields.DATABASE_NAME.field)
        YandexMetrica.reportEvent("old db migrated", "{\"items\":\"${result.size}\"}")
    }


    private fun mapMaterial(material: OldMaterial) {
        val newBrand = Brand(name = material.brand)
        val newMaterial = Material(thickness = material.thickness.toBigDecimal())
        Timber.d("THREAD map  ${Thread.currentThread().name}")
        newMaterial.brand.target = newBrand
        materialBox.put(newMaterial)
    }
}