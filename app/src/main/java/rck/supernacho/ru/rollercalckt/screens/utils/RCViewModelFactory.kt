package rck.supernacho.ru.rollercalckt.screens.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.calculation.domain.calculation.Calculable
import rck.supernacho.ru.rollercalckt.screens.calculation.view.CalculationViewModel
import rck.supernacho.ru.rollercalckt.screens.calculation.view.selector.SelectorViewModel
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.IFilterMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.view.ManageMaterialViewModel
import rck.supernacho.ru.rollercalckt.screens.material.view.MaterialsViewModel
import rck.supernacho.ru.rollercalckt.screens.preferences.view.PrefsViewModel

class RCViewModelFactory(override val kodein: Kodein): KodeinAware, ViewModelProvider.Factory {
    private val prefsRepo : IPrefRepository by instance()
    private val crudInteractor : ICrudMaterialInteractor by instance()
    private val calculator: Calculable by instance()
    private val filterInteractor: IFilterMaterialInteractor by instance()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CalculationViewModel::class.java -> CalculationViewModel(materialsInteractor = crudInteractor, preferences = prefsRepo, calculator = calculator) as T
            PrefsViewModel::class.java -> PrefsViewModel(preferences = prefsRepo) as T
            MaterialsViewModel::class.java -> MaterialsViewModel(interactor = crudInteractor, filterInteractor = filterInteractor, preferences = prefsRepo) as T
            ManageMaterialViewModel::class.java -> ManageMaterialViewModel(interactor = crudInteractor, preferenceRepo = prefsRepo) as T
            SelectorViewModel::class.java -> SelectorViewModel(materialInteractor = crudInteractor, filterInteractor = filterInteractor) as T
            else -> throw IllegalStateException("seems you forgot add new viewmodel type to factory...")
        }

    }
}