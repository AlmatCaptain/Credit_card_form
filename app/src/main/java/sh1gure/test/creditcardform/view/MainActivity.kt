package sh1gure.test.creditcardform.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import android.view.KeyEvent


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
                    etCardDate.setText(state.date)
                }
                is ProgressState.DataForNumber ->{
                    etCardNumber.setText(state.number)
                }
                is ProgressState.Empty -> {}
            }
        })
    }

    private fun initListeners() {
        btn_add_card.setOnClickListener {
            val cardNumber =
                etCardNumber.text.toString()
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
        etCardNumber.onTextChanged {
            viewModel.cursorForNumber(editTextCurrent = etCardNumber)
        }
        etCardDate.onTextChanged {
            viewModel.cursorForDate(editTextCurrent = etCardDate)
        }
        etCardNumber.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                viewModel.delete(etCardNumber)
            }
            false
        }

    }
}