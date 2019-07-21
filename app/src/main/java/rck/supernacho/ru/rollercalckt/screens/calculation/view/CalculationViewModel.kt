package rck.supernacho.ru.rollercalckt.screens.calculation.view

import androidx.lifecycle.ViewModel
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository

class CalculationViewModel(val preferences: IPrefRepository, val materials: IMaterialsRepository): ViewModel() {
}