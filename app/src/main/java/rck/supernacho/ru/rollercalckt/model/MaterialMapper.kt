package rck.supernacho.ru.rollercalckt.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import rck.supernacho.ru.rollercalckt.model.database.DataBaseFields
import rck.supernacho.ru.rollercalckt.model.database.DataBaseHelper


class MaterialMapper(context: Context) {
    private val dbHelper = DataBaseHelper(context)
    private lateinit var dataBase: SQLiteDatabase

    fun open() {
        dataBase = dbHelper.writableDatabase
    }

    fun isOpened(): Boolean{
        return dataBase.isOpen
    }

    fun close() {
        dataBase.close()
    }

    fun insert(brand: String, thickness: Double): Long {
        val conValues = ContentValues()
        if (!this.isOpened()) this.open()
        dataBase.beginTransaction()
        val id: Long
        var duplicateName = false
        var duplicateThickness = false
        try {
            var idBrand = findByBrand(brand)
            if ( idBrand <= 0){
                conValues.put(DataBaseFields.COLUMN_NAME.field, brand)
                idBrand = dataBase.insert(DataBaseFields.TABLE_MATERIALS.field, null, conValues)
            } else {
                duplicateName = true
            }
            conValues.clear()
            var idThick = findByThickness(thickness)
            if (idThick <= 0) {
                conValues.put(DataBaseFields.COLUMN_THICK.field, thickness)
                idThick = dataBase.insert(DataBaseFields.TABLE_THICKS.field, null, conValues)
            } else {
                duplicateThickness = true
            }
            conValues.clear()
            if (!duplicateName || !duplicateThickness) {
                conValues.put(DataBaseFields.COLUMN_ID_BRANDS.field, idBrand)
                conValues.put(DataBaseFields.COLUMN_ID_THICK.field, idThick)
                id = dataBase.insert(DataBaseFields.TABLE_RESULTS.field, null, conValues)
                dataBase.setTransactionSuccessful()
            } else {
                id = -1
            }
        } finally {
            dataBase.endTransaction()
        }
        return id
    }

    fun getMaterials(): ArrayList<Material>{
        val materials: ArrayList<Material> = ArrayList()
        val cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID.field
                        + ", " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field + ", "
                        + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK.field + " from " +
                        DataBaseFields.TABLE_RESULTS.field + " join " + DataBaseFields.TABLE_MATERIALS.field + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_BRANDS.field + " = " +
                        DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID.field + " join " +
                        DataBaseFields.TABLE_THICKS.field + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_THICK.field + " = " +
                        DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID.field + ";", null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            val material = Material(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2))
            materials.add(material)
            cursor.moveToNext()
        }
        cursor.close()
        return materials
    }

    fun update(material: Material, newName: String?, newThickness: Double?) {
        val ids = findByMaterial(material)
        val contentValues = ContentValues()
        dataBase.beginTransaction()
        try {

            if (newName != null && newName != material.brand) {
                contentValues.put(DataBaseFields.COLUMN_NAME.field, newName)
                dataBase.update(DataBaseFields.TABLE_MATERIALS.field, contentValues,
                        DataBaseFields.COLUMN_ID.field +
                        " = " + ids[1] + ";", null)
            }
            if (newThickness != null && newThickness != material.thickness) {
                contentValues.clear()
                contentValues.put(DataBaseFields.COLUMN_THICK.field, newThickness)
                dataBase.update(DataBaseFields.TABLE_THICKS.field, contentValues,
                        DataBaseFields.COLUMN_ID.field + " = " + ids[2] + ";", null)
            }
            dataBase.setTransactionSuccessful()
        } finally {
            dataBase.endTransaction()
        }
    }

    fun delete(material: Material) {
        dataBase.delete(DataBaseFields.TABLE_RESULTS.field, DataBaseFields.COLUMN_ID.field + " = "
                + material.id, null)
    }

    // Returns array of ids in tables 0 - Result, 1 - Material, 2 - Thickness
    private fun findByMaterial(material: Material): ArrayList<Long> {
        val cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID.field
                        + ", " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_BRANDS.field
                        + ", " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_THICK.field
                        + " from " +
                        DataBaseFields.TABLE_RESULTS.field +
                        " where " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID.field +
                        " = " + material.id + ";",
                null
        )

        val ids: ArrayList<Long> = ArrayList()
        cursor.moveToFirst()
        ids.add(cursor.getLong(0))
        ids.add(cursor.getLong(1))
        ids.add(cursor.getLong(2))
        cursor.close()
        return ids
    }
    // Returns id if brand already in base
    private fun findByBrand(brand: String): Long {
        val cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID.field
                        + ", " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field
                        + " from " + DataBaseFields.TABLE_MATERIALS.field +
                        " where " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field +
                        " = " + "'" + brand + "'" + ";",
                null
        )

        cursor.moveToFirst()
        val id: Long
        id = if( cursor.count > 0) {
            cursor.getLong(0)
        } else {
            0
        }
        cursor.close()
        return id
    }
    // Returns id if thickness already in base
    private fun findByThickness(thickness: Double): Long {
        val cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID.field
                        + ", " + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK.field
                        + " from " + DataBaseFields.TABLE_THICKS.field +
                        " where " + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK.field +
                        " = " + thickness + ";",
                null
        )

        cursor.moveToFirst()
        val id: Long
        id = if( cursor.count > 0) {
            cursor.getLong(0)
        } else {
            0
        }
        cursor.close()
        return id
    }
}
