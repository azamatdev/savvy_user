package uz.mymax.savvyenglish.network.response


import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class ThemeTestResponse(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "title")
    val title: String = "",
    @Json(name = "isFree")
    val isFree: Boolean = false,
    @Json(name = "price")
    val price: Int = 0,
    @Json(name = "testCount")
    val testCount: Int = 0,
    @Json(name = "paymentId")
    val paymentId: Int? = 0
) : Serializable




