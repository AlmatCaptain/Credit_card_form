package sh1gure.test.creditcardform.model

import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("card_number")var cardNumber: String,
    @SerializedName("card_date")var cardDate: String,
    @SerializedName("card_cvv")var cardCVV: String,
    @SerializedName("card_owner")var cardOwner: String?)