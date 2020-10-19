package rck.supernacho.ru.rollercalckt.model.repository.sharedprefs

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.model.entity.UserInput
import rck.supernacho.ru.rollercalckt.screens.preferences.view.PreferencesViewState

class PreferecesRepository(context: Context) : IPrefRepository {
    private val sharedPreferences = context.getSharedPreferences(RC_PREFS, Activity.MODE_PRIVATE)
    override var cache: PreferencesViewState? = null
        get() {
            if (field == null)
                field = getSettings()
            return field
        }

    override fun getSettings(): PreferencesViewState {
        sharedPreferences.run {
            return PreferencesViewState(
                    limits = UserInput(
                            inner = getString(RC_PREFS_INN_MAX, "0"),
                            outer = getString(RC_PREFS_OUT_MAX, "300"),
                            width = getString(RC_PREFS_LAST_WIDTH, "0")
                    ),
                    measureSystem = setMeasureSystem(getInt(RC_MEASURE_SYSTEM, 0)),
                    lastInput = UserInput(
                            inner = getString(RC_PREFS_LAST_INN, "0"),
                            outer = getString(RC_PREFS_LAST_OUT, "300"),
                            width = getString(RC_PREFS_LAST_WIDTH, "0")
                    ),
                    isWeightCalculate = getBoolean(RC_WEIGHT_ENABLED, false)
            )
        }
    }

    override fun saveSettings(vs: PreferencesViewState) {
        cache = vs
        sharedPreferences.edit {
            putString(RC_PREFS_LAST_INN, vs.lastInput.inner)
            putString(RC_PREFS_INN_MAX, vs.limits.inner)
            putString(RC_PREFS_LAST_OUT, vs.lastInput.outer)
            putString(RC_PREFS_OUT_MAX, vs.limits.outer)
            putString(RC_PREFS_LAST_WIDTH, vs.lastInput.width)
            putInt(RC_MEASURE_SYSTEM, vs.measureSystem.ordinal)
            putBoolean(RC_WEIGHT_ENABLED, vs.isWeightCalculate)
        }
    }

    private fun setMeasureSystem(ordinal: Int): MeasureSystem =
            when (ordinal) {
                1 -> MeasureSystem.IMPERIAL
                else -> MeasureSystem.METRIC
            }

    companion object {
        private const val RC_PREFS = "rc_preferences"
        private const val RC_PREFS_INN_MAX = "inner_max"
        private const val RC_PREFS_OUT_MAX = "outer_max"
        private const val RC_PREFS_LAST_INN = "inner_last"
        private const val RC_PREFS_LAST_OUT = "outer_last"
        private const val RC_PREFS_LAST_WIDTH = "width_last"
        private const val RC_MEASURE_SYSTEM = "measure_system"
        private const val RC_WEIGHT_ENABLED = "weight_enabled"
    }
}