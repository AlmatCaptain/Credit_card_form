package sh1gure.test.creditcardform.application

import android.app.Application
import sh1gure.test.creditcardform.utils.ResourceProvider

class CardForm : Application(){
    lateinit var mResourceProvider: ResourceProvider

    override fun onCreate() {
        super.onCreate()
        mResourceProvider = ResourceProvider(this)
    }
}