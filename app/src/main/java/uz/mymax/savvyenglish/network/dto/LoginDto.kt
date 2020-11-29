package uz.mymax.savvyenglish.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginDto(
    val username : String,
    val password : String
)