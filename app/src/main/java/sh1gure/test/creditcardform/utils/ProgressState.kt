package sh1gure.test.creditcardform.utils

sealed class ProgressState {
    class DataForAllert(val data: String) : ProgressState()
    class DataForDate(val date: String) : ProgressState()
    class DataForNumber(val number: String) : ProgressState()
    object NotValid : ProgressState()
    object Empty: ProgressState()
}