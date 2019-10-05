package sh1gure.test.creditcardform.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sh1gure.test.creditcardform.application.CardForm
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    application: Application
) : ViewModelProvider.Factory {
    private val applicationReference: WeakReference<CardForm> =
        WeakReference(application as CardForm)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(applicationReference.get()!!.mResourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}