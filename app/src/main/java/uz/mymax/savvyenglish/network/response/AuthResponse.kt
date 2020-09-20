package uz.mymax.savvyenglish.network.response

import com.squareup.moshi.Json

data class AuthResponse(
    @Json(name = "token")
    val token: String,
    val userName: String
)