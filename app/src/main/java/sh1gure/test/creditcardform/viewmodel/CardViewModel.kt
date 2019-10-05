package sh1gure.test.creditcardform.viewmodel

import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import sh1gure.test.creditcardform.application.CardForm
import sh1gure.test.creditcardform.utils.ProgressState
import sh1gure.test.creditcardform.model.Model
import sh1gure.test.creditcardform.utils.ResourceProvider

class CardViewModel(
    private val mResourceProvider: ResourceProvider
) : ViewModel(), LifecycleObserver {
    private val progressState: MutableLiveData<ProgressState> = MutableLiveData()

    fun addCard(cardNumber: String, cardDate: String, cardCVV: String, cardName: String) {
        if (validation(cardNumber, cardDate, cardCVV)) {
            val card = Model(cardNumber, cardDate, cardCVV, cardName)
            val gson = Gson()
            val dataJ = gson.toJson(card)
            progressState.postValue(ProgressState.DataForAllert(dataJ))
        } else {
            progressState.postValue(ProgressState.NotValid)
        }
    }

    fun cursorJump(editTextCurrent: EditText, resourceId: Int, editTextNext: EditText) {
        val length = mResourceProvider.getInteger(resourceId)
        if (editTextCurrent.length() == length) {
            editTextCurrent.clearFocus()
            editTextNext.requestFocus()
            editTextNext.isCursorVisible = true
        }
    }

    fun hideKeyboard(editTextCurrent: EditText, resourceId: Int) {
        if (editTextCurrent.length() == resourceId)
            progressState.postValue(ProgressState.HideKeyBoard)
    }

    fun cursorForDate(editTextCurrent: EditText, resourceId: Int, editTextNext: EditText) {
        val data = editTextCurrent.text.toString()
        when {
            editTextCurrent.length() == 3 -> {dateChange(date = data)}
            editTextCurrent.length() == 5 -> { cursorJump(editTextCurrent = editTextCurrent, resourceId = resourceId, editTextNext = editTextNext) }
        }
    }

    private fun dateChange(date: String) {
        val dateCheck = if (date.substring(0,2).toInt() > 12) {
            "12/" + date.substring(2, date.length)
        } else {
            date.substring(0, 2) + "/" + date.substring(2, date.length)
        }
        progressState.postValue(ProgressState.DataForDate(date = dateCheck))
    }

    fun getCardData(): MutableLiveData<ProgressState> {
        return progressState
    }

    private fun validation(cardNumber: String, cardDate: String, cardCVV: String): Boolean {
        return (cardNumber.length == 16 && cardDate.length == 5 && cardCVV.length == 3)
    }

    fun clearForm(viewGroup: ViewGroup) {
        var i = 0
        val count = viewGroup.childCount
        while (i < count) {
            val view = viewGroup.getChildAt(i)
            if (view is EditText) {
                view.setText("")
            }

            if (view is ViewGroup && view.childCount > 0)
                clearForm(view)
            ++i
        }
        progressState.postValue(ProgressState.Empty)
    }
}