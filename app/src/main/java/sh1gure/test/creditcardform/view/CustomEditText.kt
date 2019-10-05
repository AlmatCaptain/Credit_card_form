package sh1gure.test.creditcardform.view

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText


class CustomEditText(context: Context, attrs: AttributeSet? = null) : EditText(context, attrs){

    public override fun onSelectionChanged(start: Int, end: Int) {

        val text = text
        if (text != null) {
            if (start != text.length || end != text.length) {
                setSelection(text.length, text.length)
                return
            }
        }

        super.onSelectionChanged(start, end)
    }

}