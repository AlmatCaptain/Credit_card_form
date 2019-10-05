package sh1gure.test.creditcardform.utils

sealed class ProgressState {
    class DataForAllert(val data: String) : ProgressState()
    class DataForDate(val date: String) : ProgressState()
    object NotValid : ProgressState()
    object HideKeyBoard: ProgressState()
    object Empty: ProgressState()
}