package rck.supernacho.ru.rollercalckt.model.helper

import android.content.Context
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import rck.supernacho.ru.rollercalckt.BuildConfig
import rck.supernacho.ru.rollercalckt.model.entity.MyObjectBox
import timber.log.Timber


object ObjectBox{

    private lateinit var boxStore: BoxStore

    fun init(context: Context) {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        if (BuildConfig.DEBUG) {
            Timber.d("Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }
   fun <T> getRepository(entity: Class<T>): Box<T> = boxStore.boxFor(entity)
}