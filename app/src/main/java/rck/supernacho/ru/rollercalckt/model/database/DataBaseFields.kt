package rck.supernacho.ru.rollercalckt.model.database

enum class DataBaseFields(val field: String) {

    DATABASE_NAME("rc_materials.db"),
    DATABASE_VERSION("2"),
    TABLE_MATERIALS("brands"),
    TABLE_THICKS("thicks"),
    TABLE_RESULTS("results"),
    COLUMN_ID("_id"),
    COLUMN_NAME("name"),
    COLUMN_THICK("thick"),
    COLUMN_ID_BRANDS("id_brands"),
    COLUMN_ID_THICK("id_thick")
}