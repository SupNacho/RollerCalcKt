package rck.supernacho.ru.rollercalckt.modelnew.helper

import android.content.Context
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import rck.supernacho.ru.rollercalckt.BuildConfig
import rck.supernacho.ru.rollercalckt.model.repository.database.IMaterialsRepository
import rck.supernacho.ru.rollercalckt.modelnew.entity.MyObjectBox
import timber.log.Timber


object ObjectBox : IMaterialsRepository{

    private lateinit var boxStore: BoxStore

    fun init(context: Context) {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        if (BuildConfig.DEBUG) {
            Timber.d("Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }
    override fun getRepository(): BoxStore = boxStore
}