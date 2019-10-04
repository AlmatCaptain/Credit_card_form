package sh1gure.test.creditcardform.viewmodel

import android.widget.EditText
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sh1gure.test.creditcardform.ProgressState
import sh1gure.test.creditcardform.model.Model

class CardViewModel: ViewModel(), LifecycleObserver {

    private val progressState: MutableLiveData<ProgressState> = MutableLiveData()
    private var validationArray = arrayListOf<Int>()
    private val checkArray = arrayListOf(0,1,2,3,4,5)

    fun addCard(cardNumber: String, cardDate: String, cardCVV: String, cardName: String){
        if(validation()){
            val card = Model(cardNumber, cardDate, cardCVV, cardName)
            progressState.postValue(ProgressState.DataForAllert(card))
        }else{
            progressState.postValue(ProgressState.NotValid)
        }
    }

    fun cursorJump(editTextCurrent: EditText, number: Int, editTextNext: EditText, fieldId: Int) {
        if (editTextCurrent.length() == number) {
            editTextCurrent.clearFocus()
            editTextNext.requestFocus()
            editTextNext.isCursorVisible = true
            pushFilled(fieldId)
        }
    }

    fun dateChange(date: String){
        val dateCheck = if(date.toInt()>12){
            "12/"
        }else {
            "$date/"
        }
        progressState.postValue(ProgressState.DataForDate(dateCheck))
    }

    fun getCardData(): MutableLiveData<ProgressState>{
        return  progressState
    }

    fun pushFilled(id: Int){
        validationArray.add(id)
    }

    private fun validation():Boolean{
        return validationArray.containsAll(checkArray)
    }

    fun clear(){
        validationArray.clear()
    }

}