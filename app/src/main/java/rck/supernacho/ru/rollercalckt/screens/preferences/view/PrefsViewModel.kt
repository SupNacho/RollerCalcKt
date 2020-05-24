package rck.supernacho.ru.rollercalckt.screens.preferences.view

import androidx.lifecycle.ViewModel
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository

class PrefsViewModel(val preferences: IPrefRepository): ViewModel() {
    val viewState: PreferencesViewState = preferences.getSettings()

    fun saveState(){
        preferences.saveSettings(viewState)
    }

    fun chooseMeasureSystem(system: MeasureSystem){
        viewState.run{
            measureSystem = system
        }
        saveState()
    }

    fun enableWeightCalculation(isEnabled: Boolean){
        viewState.isWeightCalculate = isEnabled
        saveState()
    }
}