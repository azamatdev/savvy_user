package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AuthResponse(
    @Json(name = "token")
    val token: String,
    val userName: String
)