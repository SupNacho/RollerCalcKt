package rck.supernacho.ru.rollercalckt.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import rck.supernacho.ru.rollercalckt.model.database.DataBaseFields
import rck.supernacho.ru.rollercalckt.model.database.DataBaseHelper


class MaterialMapper(private val context: Context) {
    private val dbHelper = DataBaseHelper(context)
    private lateinit var dataBase: SQLiteDatabase

    fun open() {
        dataBase = dbHelper.writableDatabase
    }

    fun close() {
        dataBase.close()
    }

    fun insert(brand: String, thickness: Double) {
        val conValues = ContentValues()
        dataBase.beginTransaction()
        try {
            conValues.put(DataBaseFields.COLUMN_NAME.field, brand)
            val idBrand = dataBase.insert(DataBaseFields.TABLE_MATERIALS.field, null, conValues)
            conValues.clear()
            conValues.put(DataBaseFields.COLUMN_THICK.field, thickness)
            val idThick = dataBase.insert(DataBaseFields.TABLE_THICKS.field, null, conValues)
            conValues.clear()
            conValues.put(DataBaseFields.COLUMN_ID_BRANDS.field, idBrand)
            conValues.put(DataBaseFields.COLUMN_ID_THICK.field, idThick)
            dataBase.insert(DataBaseFields.TABLE_RESULTS.field, null, conValues)
            dataBase.setTransactionSuccessful()
        } finally {
            dataBase.endTransaction()
        }
    }

    fun getMaterials(): ArrayList<Material>{
        val materials: ArrayList<Material> = ArrayList()
        val cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field + ", "
                        + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK + " from " +
                        DataBaseFields.TABLE_RESULTS + " join " + DataBaseFields.TABLE_MATERIALS.field + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_BRANDS.field + " = " +
                        DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID + " join " +
                        DataBaseFields.TABLE_THICKS + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_THICK.field + " = " +
                        DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID + ";", null
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

            if (newName != null) {
                contentValues.put(DataBaseFields.COLUMN_NAME.field, newName)
                dataBase.update(DataBaseFields.TABLE_MATERIALS.field, contentValues,
                        DataBaseFields.COLUMN_ID.field +
                        " = " + ids[0] + ";", null)
            }
            if (newThickness != null) {
                contentValues.clear()
                contentValues.put(DataBaseFields.COLUMN_THICK.field, newThickness)
                dataBase.update(DataBaseFields.TABLE_THICKS.field, contentValues,
                        DataBaseFields.COLUMN_ID.field + " = " + ids[1] + ";", null)
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

    // Returns array of ids in tables 0 - Material, 1 - Thicks, 2 - Result
    fun findByMaterial(material: Material): ArrayList<Long> {
        var cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field + ", "
                        + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK + " from " +
                        DataBaseFields.TABLE_RESULTS + " join " + DataBaseFields.TABLE_MATERIALS.field + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_BRANDS.field + " = " +
                        DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID + " join " +
                        DataBaseFields.TABLE_THICKS + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_THICK.field + " = " +
                        DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID +
                        " where " + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID +
                        " = " + material.id + " and " + DataBaseFields.TABLE_MATERIALS + "." + DataBaseFields.COLUMN_NAME +
                        " and " + DataBaseFields.TABLE_THICKS + "." + DataBaseFields.COLUMN_THICK + " = " + material.thickness + ";",
                null
        )

        val ids: ArrayList<Long> = ArrayList()
        ids.add(cursor.getLong(0))
        ids.add(cursor.getLong(1))
        ids.add(cursor.getLong(2))
        cursor.close()
        return ids
    }

    // Returns id in thickness table
    fun findByThickness(thickness: Double): Long {
        var cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID
                        + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field + ", "
                        + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK + " from " +
                        DataBaseFields.TABLE_RESULTS + " join " + DataBaseFields.TABLE_MATERIALS.field + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_BRANDS.field + " = " +
                        DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID + " join " +
                        DataBaseFields.TABLE_THICKS + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_THICK.field + " = " +
                        DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID +
                        " where " + DataBaseFields.TABLE_THICKS + "." + DataBaseFields.COLUMN_THICK +
                        " = " + thickness + ";",
                null
        )
        val idThickness = cursor.getLong(1)
        return idThickness
    }

    //Return id in materials table
    fun findByName(brandName: String): Long {
        var cursor = dataBase.rawQuery(
                "select " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID
                        + DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID
                        + ", " + DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_NAME.field + ", "
                        + DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_THICK + " from " +
                        DataBaseFields.TABLE_RESULTS + " join " + DataBaseFields.TABLE_MATERIALS.field + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_BRANDS.field + " = " +
                        DataBaseFields.TABLE_MATERIALS.field + "." + DataBaseFields.COLUMN_ID + " join " +
                        DataBaseFields.TABLE_THICKS + " on " +
                        DataBaseFields.TABLE_RESULTS.field + "." + DataBaseFields.COLUMN_ID_THICK.field + " = " +
                        DataBaseFields.TABLE_THICKS.field + "." + DataBaseFields.COLUMN_ID +
                        " where " + DataBaseFields.TABLE_MATERIALS + "." + DataBaseFields.COLUMN_NAME + " = " +
                        brandName + ";",
                null
        )
        val idMaterials = cursor.getLong(0)
        cursor.close()
        return idMaterials
    }


}
