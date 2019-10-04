package sh1gure.test.creditcardform

import sh1gure.test.creditcardform.model.Model

sealed class ProgressState {
    class DataForAllert(val data: Model) : ProgressState()
    class DataForDate(val date: String) : ProgressState()
    object NotValid : ProgressState()
}