package rck.supernacho.ru.rollercalckt.controller

import android.app.Activity
import android.content.Context

class PrefsController(private val context: Context) {

    private val RC_PREFS = "rc_preferences"
    private val RC_PREFS_INN_MAX = "inner_max"
    private val RC_PREFS_OUT_MAX = "outer_max"
    private val RC_PREFS_LAST_INN = "inner_last"
    private val RC_PREFS_LAST_OUT = "outer_last"
    private val prefs = context.getSharedPreferences(RC_PREFS, Activity.MODE_PRIVATE)

    fun setInnerMax(innMax: String){
        prefs.edit().putString(RC_PREFS_INN_MAX, innMax).apply()
    }

    fun setOuterMax(outMax: String){
        prefs.edit().putString(RC_PREFS_OUT_MAX, outMax).apply()
    }

    fun setLastInner(innLast: String){
        prefs.edit().putString(RC_PREFS_LAST_INN, innLast).apply()
    }

    fun setLastOuter(outLast: String){
        prefs.edit().putString(RC_PREFS_LAST_OUT, outLast).apply()
    }

    fun getInnerMax(): String {
        return prefs.getString(RC_PREFS_INN_MAX, "150")
    }

    fun getOuterMax(): String {
        return prefs.getString(RC_PREFS_OUT_MAX, "300")
    }

    fun getInnerLast(): String {
        return prefs.getString(RC_PREFS_LAST_INN, "82")
    }

    fun getOuterLast(): String {
        return prefs.getString(RC_PREFS_LAST_OUT, "144")
    }
}