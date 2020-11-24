package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class VariantTestResponse(
    @Json(name = "testCount")
    val testCount: Int = 0,
    @Json(name = "isFree")
    val isFree: Boolean = false,
    @Json(name = "price")
    val price: Int = 0,
    @Json(name = "bestResult")
    val bestResult: Int = 0,
    @Json(name = "attemps")
    val attemps: Int = 0,
    @Json(name = "paymentId")
    val paymentId: Int? = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "themeId")
    val themeId: Int? = 0,
    @Json(name = "title")
    val title: String = ""
)