package sh1gure.test.creditcardform.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import sh1gure.test.creditcardform.utils.ProgressState
import sh1gure.test.creditcardform.viewmodel.CardViewModel
import sh1gure.test.creditcardform.viewmodel.ViewModelFactory
import sh1gure.test.creditcardform.R
import sh1gure.test.creditcardform.utils.extentions.onTextChanged

class MainActivity : AppCompatActivity() {

    private val viewModel: CardViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(application)).get(CardViewModel::class.java)
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
                    etCardDate.setText(state.date)
                }
                is ProgressState.HideKeyBoard -> {
                    etCardCvv.isCursorVisible = false
                    val inputManager: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.SHOW_FORCED
                    )
                    etCardCvv.clearFocus()
                }
                is ProgressState.Empty -> {}
            }
        })
    }

    private fun initListeners() {
        btn_add_card.setOnClickListener {
            val cardNumber =
                etCardNumber1.text.toString() + etCardNumber2.text.toString() + etCardNumber3.text.toString() + etCardNumber4.text.toString()
            val cardDate = etCardDate.text.toString()
            val cardCVV = etCardCvv.text.toString()
            val cardName = etCardName.text.toString()
            viewModel.addCard(cardNumber, cardDate, cardCVV, cardName)
        }
    }

    private fun alertDialog(data: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Your Card")
        alertDialogBuilder.setMessage(data)
        alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
            viewModel.clearForm(viewGroup = rootView)
        }
        alertDialogBuilder.show()
    }

    private fun initEdit() {
        etCardNumber1.onTextChanged {
            viewModel.cursorJump(editTextCurrent = etCardNumber1, resourceId = R.integer.max_length_card_number_field, editTextNext = etCardNumber2)
        }
        etCardNumber2.onTextChanged {
            viewModel.cursorJump(editTextCurrent = etCardNumber2, resourceId = R.integer.max_length_card_number_field, editTextNext = etCardNumber3)
        }
        etCardNumber3.onTextChanged {
            viewModel.cursorJump(editTextCurrent = etCardNumber3, resourceId = R.integer.max_length_card_number_field, editTextNext = etCardNumber4
            )
        }
        etCardNumber4.onTextChanged {
            viewModel.cursorJump(editTextCurrent = etCardNumber4, resourceId = R.integer.max_length_card_number_field, editTextNext =  etCardDate)
        }
        etCardDate.onTextChanged {
            viewModel.cursorForDate(editTextCurrent = etCardDate, resourceId = R.integer.max_length_date, editTextNext = etCardCvv)
        }
        etCardCvv.onTextChanged {
            viewModel.hideKeyboard(editTextCurrent = etCardCvv, resourceId = R.integer.max_length_cvv)
        }
    }
}