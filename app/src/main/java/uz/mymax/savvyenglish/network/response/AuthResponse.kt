package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
@Keep
data class AuthResponse(
    @Json(name = "token")
    val token: String,
    @Json(name = "userData")
    val userdata: UserData
)
@Keep
data class UserData(
    @Json(name = "role")
    val role: String,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "username")
    val username: String
)