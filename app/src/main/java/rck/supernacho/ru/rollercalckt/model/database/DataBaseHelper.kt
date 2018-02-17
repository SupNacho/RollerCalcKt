package rck.supernacho.ru.rollercalckt.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBaseHelper(context: Context) : SQLiteOpenHelper(
        context,
        DataBaseFields.DATABASE_NAME.field,
        null,
        DataBaseFields.DATABASE_VERSION.field.toInt()
) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE" + DataBaseFields.TABLE_MATERIALS.field + "(\n" +
                DataBaseFields.COLUMN_ID.field + " INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 NOT NULL,\n" +
                DataBaseFields.COLUMN_NAME.field + " TEXT\n" +
                ");")
        db?.execSQL("CREATE TABLE " +  DataBaseFields.TABLE_THICKS.field + " (\n" +
                DataBaseFields.COLUMN_ID.field + " INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                  NOT NULL,\n" +
                DataBaseFields.COLUMN_THICK.field + " DOUBLE  NOT NULL\n" +
                ");")
        db?.execSQL("CREATE TABLE " +  DataBaseFields.TABLE_RESULTS.field + " (\n" +
                DataBaseFields.COLUMN_ID.field + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                DataBaseFields.COLUMN_ID_BRANDS.field + " INTEGER REFERENCES brands (_id) ON DELETE CASCADE\n" +
                "                                              ON UPDATE CASCADE\n" +
                "                      NOT NULL,\n" +
                DataBaseFields.COLUMN_ID_THICK.field + " INTEGER REFERENCES thicks (_id) ON DELETE CASCADE\n" +
                "                                              ON UPDATE CASCADE\n" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS " +  DataBaseFields.TABLE_MATERIALS.field)
            onCreate(db)
        }
    }
}