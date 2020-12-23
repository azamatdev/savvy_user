package uz.mymax.savvyenglish.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterDto(
    var email : String,
    var password : String,
    var username : String,
    var phonenumber : String

)