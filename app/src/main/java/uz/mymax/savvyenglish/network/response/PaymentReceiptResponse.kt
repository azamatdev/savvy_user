package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep

@Keep
data class PaymentReceiptResponse(
    val amount: Int,
    val cancelTime: Int,
    val checkId: String,
    val createTime: Long,
    val isTheme: Boolean,
    val payTime: Int,
    val state: Int,
    val themeOrTestId: Int,
    val userId: Int
)