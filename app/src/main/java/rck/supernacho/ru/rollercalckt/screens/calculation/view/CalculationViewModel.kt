package rck.supernacho.ru.rollercalckt.screens.calculation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.model.repository.sharedprefs.IPrefRepository
import rck.supernacho.ru.rollercalckt.screens.calculation.domain.calculation.Calculable
import rck.supernacho.ru.rollercalckt.screens.material.domain.FilterInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.ICrudMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.material.domain.IFilterMaterialInteractor
import rck.supernacho.ru.rollercalckt.screens.preferences.domain.toImperialLength
import rck.supernacho.ru.rollercalckt.screens.preferences.domain.toImperialWeight
import rck.supernacho.ru.rollercalckt.screens.preferences.domain.toMetricThickness
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

class CalculationViewModel(
        private val materialsInteractor: ICrudMaterialInteractor,
        private val preferences: IPrefRepository,
        private val calculator: Calculable
) : ViewModel() {
    val materialsList: LiveData<List<MaterialUi>> by lazy {
        initMaterialLiveData()
    }
    val viewState: LiveData<CalcViewState> by lazy {
        initViewState()
    }

    private fun initViewState(): LiveData<CalcViewState> {
        val liveData = MutableLiveData<CalcViewState>()
        val prefs = preferences.getSettings()
        liveData.value = CalcViewState(
                measureSystem = prefs.measureSystem,
                innerInput = prefs.lastInput.inner?.toBigDecimal() ?: BigDecimal.ZERO,
                outerInput = prefs.lastInput.outer?.toBigDecimal() ?: BigDecimal.ZERO,
                innerInputMicrons = BigDecimal.ZERO,
                outerInputMicrons = BigDecimal.ZERO,
                thickness = BigDecimal.ZERO,
                resultLength = BigDecimal.ZERO,
                resultWeight = BigDecimal.ZERO,
                weight = BigDecimal.ZERO,
                width = BigDecimal.ZERO,
                density = BigDecimal.ZERO,
                preferencesViewState = prefs
        )
        return liveData
    }

    val filterInteractor: IFilterMaterialInteractor = FilterInteractor(materialsList as MutableLiveData)
    private val dataSubscription = materialsInteractor.dataSubscription
            .subscribeOn(Schedulers.io())
            .subscribe { data ->
                Timber.d("Updates received: $data")
                val updatedData = materialsInteractor.getMaterials()
                (materialsList as MutableLiveData<List<MaterialUi>>).postValue(updatedData)
                filterInteractor.tempCollection = updatedData
            }

    private fun initMaterialLiveData(): LiveData<List<MaterialUi>> {
        val liveData = MutableLiveData<List<MaterialUi>>()
        liveData.postValue(materialsInteractor.getMaterials())
        return liveData
    }

    private fun calculate() {
        val vs = viewState.value
        vs?.let {
            if (it.measureSystem == MeasureSystem.IMPERIAL) {
                calculateWith(it, it.innerInput.toMetricThickness(), it.outerInput.toMetricThickness(),
                        it.thickness.toMetricThickness(), it.width.toMetricThickness())
            } else {
                calculateWith(it, it.innerInput, it.outerInput, it.thickness, it.width)
            }
        }
    }

    private fun calculateWith(vs: CalcViewState, inner: BigDecimal, outer: BigDecimal, thickness: BigDecimal, width: BigDecimal) {
        val length = calculator.getLength(outer, inner, thickness)
        val weight = calculator.getWeight(length, width, thickness, vs.weight, vs.density)
        val preparedResult = if (vs.measureSystem == MeasureSystem.IMPERIAL) {
            Pair(
                    length.toImperialLength().setScale(5, RoundingMode.HALF_UP),
                    weight.toImperialWeight().setScale(2, RoundingMode.HALF_UP)
            )
        } else
            Pair(length, weight)

        (viewState as MutableLiveData).value =
                vs.copy(resultLength = preparedResult.first, resultWeight = preparedResult.second)
    }

    fun onSelectedMaterial(position: Int) {
        materialsList.value?.let { materials ->
            val material = materials[position]

            (viewState as MutableLiveData).value =
                    viewState.value?.copy(
                            thickness = material.thickness?.toBigDecimal()?: BigDecimal.ZERO,
                            weight = material.weight?.toBigDecimal()?: BigDecimal.ZERO,
                            density = material.density?.toBigDecimal()?: BigDecimal.ZERO
                            )

            calculate()
        }
    }

    fun setWidth(input: String){
        //TODO fix check for empty field
        (viewState as MutableLiveData).value = viewState.value?.copy(width = input.toBigDecimal())
        calculate()
    }

    fun setInput(input: String, isInner: Boolean) {
        (viewState as MutableLiveData).value =
                if (isInner)
                    viewState.value?.copy(innerInput = input.toBigDecimal())
                else
                    viewState.value?.copy(outerInput = input.toBigDecimal())
        calculate()
    }

    override fun onCleared() {
        super.onCleared()
        dataSubscription.dispose()
    }
}