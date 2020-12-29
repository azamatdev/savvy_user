package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class VariantTestResponse(
    @Json(name = "testCount")
    val testCount: Int = 0,
    @Json(name = "isFree")
    var isFree: Boolean = false,
    @Json(name = "isPaid")
    var isPaid: Boolean = false,
    @Json(name = "price")
    var price: Int = 0,
    @Json(name = "bestResult")
    val bestResult: Int = 0,
    @Json(name = "attemps")
    val attemps: Int = 0,
    @Json(name = "paymentId")
    var paymentId: Int? = 0,
    @Json(name = "id")
    var id: Int = 0,
    @Json(name = "themeId")
    var themeId: Int? = 0,
    @Json(name = "title")
    var title: String = ""
)