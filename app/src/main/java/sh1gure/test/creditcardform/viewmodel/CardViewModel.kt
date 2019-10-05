package sh1gure.test.creditcardform.viewmodel

import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import sh1gure.test.creditcardform.utils.ProgressState
import sh1gure.test.creditcardform.model.Model

class CardViewModel : ViewModel(), LifecycleObserver {
    private val spaces = "          "
    private val progressState: MutableLiveData<ProgressState> = MutableLiveData()

    fun addCard(cardNumber: String, cardDate: String, cardCVV: String, cardName: String) {
        val cardNumberEdited = cardNumber.replace(" ","")
        if (validation(cardNumberEdited, cardDate, cardCVV)) {
            val card = Model(cardNumberEdited, cardDate, cardCVV, cardName)
            val gson = Gson()
            val dataJ = gson.toJson(card)
            progressState.postValue(ProgressState.DataForAllert(dataJ))
        } else {
            progressState.postValue(ProgressState.NotValid)
        }
    }

    fun cursorForDate(editTextCurrent: EditText) {
        val data = editTextCurrent.text.toString()
        if (editTextCurrent.length() == 3)
            dateChange(date = data)
    }


    fun cursorForNumber(editTextCurrent: EditText) {
        val data = editTextCurrent.text.toString()
        when (editTextCurrent.length()) {
            4 -> {numberDivider(data)}
            16 -> {fixTypo(data)}
            18 -> {numberDivider(data)}
            32 -> {numberDivider(data)}
        }
    }

    private fun fixTypo(data: String){
        if (!data.contains(' ')){
            val dataC = data.substring(0,4) + spaces + data.substring(4,8) + spaces + data.substring(8, 12) + spaces + data.substring(12,16)
            progressState.postValue(ProgressState.DataForNumber(dataC))
        }
    }

    private fun numberDivider(data: String){
        progressState.postValue(ProgressState.DataForNumber(number = data + spaces))
    }

    fun delete(editTextCurrent: EditText){
        val data = editTextCurrent.text.toString()
        when (editTextCurrent.length()) {
            14 -> {onDeleteChange(data, 15)}
            28 -> {onDeleteChange(data, 29)}
            42 -> {onDeleteChange(data, 43)}
        }
    }

    private fun onDeleteChange(data: String, position: Int){
        val dataC = data.substring(0, position - 11)
        progressState.postValue(ProgressState.DataForNumber(dataC))
    }

    private fun dateChange(date: String) {
        val dateCheck = if (date.substring(0, 2).toInt() > 12) {
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