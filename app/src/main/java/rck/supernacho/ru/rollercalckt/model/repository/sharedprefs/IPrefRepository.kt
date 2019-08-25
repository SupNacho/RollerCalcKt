package rck.supernacho.ru.rollercalckt.model.repository.sharedprefs

import rck.supernacho.ru.rollercalckt.screens.preferences.view.PreferencesViewState

interface IPrefRepository {
    var cache: PreferencesViewState?
    fun getSettings(): PreferencesViewState
    fun saveSettings(vs : PreferencesViewState)
}