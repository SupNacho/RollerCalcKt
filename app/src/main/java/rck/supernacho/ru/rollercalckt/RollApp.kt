package rck.supernacho.ru.rollercalckt

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import rck.supernacho.ru.rollercalckt.di.appModule

class RollApp: Application(), KodeinAware {

    override val kodein: Kodein = Kodein {
        import(appModule)
        import(androidXModule(this@RollApp))
    }
    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this))
            return
        LeakCanary.install(this)
    }
}