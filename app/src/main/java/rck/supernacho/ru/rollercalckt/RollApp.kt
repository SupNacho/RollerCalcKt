package rck.supernacho.ru.rollercalckt

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import rck.supernacho.ru.rollercalckt.di.appModule
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import rck.supernacho.ru.rollercalckt.modelnew.helper.ObjectBox


class RollApp: Application(), KodeinAware {

    override val kodein: Kodein = Kodein {
        import(androidXModule(this@RollApp))
        import(appModule)
    }
    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
        initAppMetrika()
        ObjectBox.init(this)
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this))
            return
        LeakCanary.install(this)
    }

    private fun initAppMetrika(){
        // Creating an extended library configuration.
        val config = YandexMetricaConfig.newConfigBuilder(getString(R.string.app_metrika)).build()
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this)
    }
}