package rck.supernacho.ru.rollercalckt.screens.material.view

import androidx.lifecycle.ViewModel
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository

class MaterialsViewModel( val preferences: IPrefRepository,  val materials: IMaterialsRepository): ViewModel() {
}