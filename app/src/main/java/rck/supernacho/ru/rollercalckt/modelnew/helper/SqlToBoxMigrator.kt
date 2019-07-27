package rck.supernacho.ru.rollercalckt.modelnew.helper

import android.content.Context
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import rck.supernacho.ru.rollercalckt.model.MaterialMapper
import rck.supernacho.ru.rollercalckt.modelnew.entity.Brand
import rck.supernacho.ru.rollercalckt.modelnew.entity.Material
import timber.log.Timber
import java.lang.Exception

typealias OldMaterial = rck.supernacho.ru.rollercalckt.model.Material

object SqlToBoxMigrator {
    private val materialBox: Box<Material> = ObjectBox.getRepository().boxFor()

    fun checkAndMigrate(context: Context): Boolean {
        val oldDB = MaterialMapper(context)
        oldDB.open()
        val result = oldDB.getMaterials()
        return if (result.isEmpty())
            true
        else {
            try {
                result.forEach { m -> mapMaterial(m) }
                true
            } catch (e: Exception) {
                Timber.e(e)
                false
            }
        }
    }


    private fun mapMaterial(material: OldMaterial) {
        val newBrand = Brand(name = material.brand)
        val newMaterial = Material(thickness = material.thickness.toBigDecimal())
        newMaterial.brand.target = newBrand
        materialBox.put(newMaterial)
    }
}