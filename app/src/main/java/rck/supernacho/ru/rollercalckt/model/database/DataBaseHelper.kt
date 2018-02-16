package rck.supernacho.ru.rollercalckt.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBaseHelper : SQLiteOpenHelper {
    constructor(context: Context) : super(
            context,
            DATABASE_NAME,
            null,
            DATABASE_VERSION
    )

    companion object {
        private val DATABASE_NAME = "rc_materials.db"
        private val DATABASE_VERSION = 0
        private val TABLE_MATERIALS = "brands"
        private val TABLE_THICKS = "thicks"
        private val TABLE_RESULTS = "results"
        private val COLUMN_ID = "_id"
        private val COLUMN_NAME = "name"
        private val COLUMN_THICK = "thick"
        private val COLUMN_ID_BRANDS = "id_brands"
        private val COLUMN_ID_THICK = "id_thick"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE" + TABLE_MATERIALS + "(\n" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 NOT NULL,\n" +
                COLUMN_NAME + " TEXT\n" +
                ");")
        db?.execSQL("CREATE TABLE " + TABLE_THICKS + " (\n" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                  NOT NULL,\n" +
                COLUMN_THICK + " DOUBLE  NOT NULL\n" +
                ");")
        db?.execSQL("CREATE TABLE " + TABLE_RESULTS + " (\n" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COLUMN_ID_BRANDS + " INTEGER REFERENCES brands (_id) ON DELETE CASCADE\n" +
                "                                              ON UPDATE CASCADE\n" +
                "                      NOT NULL,\n" +
                COLUMN_ID_THICK + " INTEGER REFERENCES thicks (_id) ON DELETE CASCADE\n" +
                "                                              ON UPDATE CASCADE\n" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIALS)
            onCreate(db)
        }
    }
}