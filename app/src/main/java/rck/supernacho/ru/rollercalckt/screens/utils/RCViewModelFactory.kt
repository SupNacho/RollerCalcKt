package rck.supernacho.ru.rollercalckt.screens.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.calculation.view.CalculationViewModel
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.view.EditMaterialViewModel
import rck.supernacho.ru.rollercalckt.screens.material.view.MaterialsViewModel
import rck.supernacho.ru.rollercalckt.screens.preferences.view.PrefsViewModel

class RCViewModelFactory(override val kodein: Kodein): KodeinAware, ViewModelProvider.Factory {
    private val prefsRepo : IPrefRepository by instance()
    private val crudInteractor : ICrudMaterialInteractor by instance()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CalculationViewModel::class.java -> CalculationViewModel(materialsInteractor = crudInteractor) as T
            PrefsViewModel::class.java -> PrefsViewModel(preferences = prefsRepo) as T
            MaterialsViewModel::class.java -> MaterialsViewModel(interactor = crudInteractor) as T
            EditMaterialViewModel::class.java -> EditMaterialViewModel(interactor = crudInteractor) as T
            else -> throw IllegalStateException("seems you forgot add new viewmodel type to factory...")
        }

    }
}