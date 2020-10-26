package rck.supernacho.ru.rollercalckt.model.entity

data class UserInput(
        var inner: String?,
        var outer: String?,
        val width: String?,
        val lastSelectedMaterial: Long
)