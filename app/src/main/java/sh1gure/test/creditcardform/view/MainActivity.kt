package sh1gure.test.creditcardform.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import sh1gure.test.creditcardform.ProgressState
import sh1gure.test.creditcardform.model.Model
import sh1gure.test.creditcardform.viewmodel.CardViewModel
import sh1gure.test.creditcardform.viewmodel.ViewModelFactory
import android.view.ViewGroup
import sh1gure.test.creditcardform.R

class MainActivity : AppCompatActivity() {

    private val viewModel: CardViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(CardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
        initObserver()
        initEdit()
    }

    private fun initObserver() {
        viewModel.getCardData().observe(this, Observer { state ->
            when (state) {
                is ProgressState.NotValid -> {
                    Toast.makeText(this, "Fill all necessary fields", Toast.LENGTH_LONG).show()
                }
                is ProgressState.DataForAllert -> {
                    alertDialog(state.data)
                }
                is ProgressState.DataForDate -> {
                    et_card_date.setText(state.date)
                    et_card_date.setSelection(3)
                }
            }
        })
    }

    private fun initListeners() {
        btn_add_card.setOnClickListener {
            val cardNumber =
                et_card_number_1.text.toString() + et_card_number_2.text.toString() + et_card_number_3.text.toString() + et_card_number_4.text.toString()
            val cardDate = et_card_date.text.toString()
            val cardCVV = et_card_cvv.text.toString()
            val cardName = et_card_name.text.toString()
            viewModel.addCard(cardNumber, cardDate, cardCVV, cardName)
        }
    }

    private fun alertDialog(data: Model) {
        val gson = Gson()
        val dataJ = gson.toJson(data)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Your Card")
        alertDialogBuilder.setMessage(dataJ)
        alertDialogBuilder.setPositiveButton(android.R.string.ok) { dialog, which ->
            clearForm(rootView)
        }
        alertDialogBuilder.show()
    }

    private fun initEdit() {
        et_card_number_1.onTextChanged {
            viewModel.cursorJump(et_card_number_1, 4, et_card_number_2, 0)
        }
        et_card_number_2.onTextChanged {
            viewModel.cursorJump(et_card_number_2, 4, et_card_number_3,1)
        }
        et_card_number_3.onTextChanged {
            viewModel.cursorJump(et_card_number_3, 4, et_card_number_4,2)
        }
        et_card_number_4.onTextChanged {
            viewModel.cursorJump(et_card_number_4, 4, et_card_date,3)
        }
        et_card_date.onTextChanged {
            if (et_card_date.length() == 2) {
                viewModel.dateChange(et_card_date.text.toString())
            }
            viewModel.cursorJump(et_card_date, 5, et_card_cvv,4)
        }
        et_card_cvv.onTextChanged {
            if (et_card_cvv.length() == 3) {
                et_card_cvv.isCursorVisible = false
                val inputManager: InputMethodManager =getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
                et_card_cvv.clearFocus()
                viewModel.pushFilled(5)
            }
        }
    }

    private fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                onTextChanged.invoke(editable.toString())
            }
        })
    }

    private fun clearForm(group: ViewGroup) {
        var i = 0
        val count = group.childCount
        while (i < count) {
            val view = group.getChildAt(i)
            if (view is EditText) {
                view.setText("")
            }

            if (view is ViewGroup && view.childCount > 0)
                clearForm(view)
            ++i
        }
        viewModel.clear()
    }
}
