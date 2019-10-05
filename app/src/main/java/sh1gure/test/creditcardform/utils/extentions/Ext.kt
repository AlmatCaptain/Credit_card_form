package sh1gure.test.creditcardform.utils.extentions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

var isDelete = false

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            isDelete = p2 != 0
        }

        override fun afterTextChanged(editable: Editable?) {
            if(!isDelete)
                onTextChanged.invoke(editable.toString())
        }
    })
}