package uz.mymax.savvyenglish.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterDto(
    var name : String,
    var email : String,
    var phone : String,
    var password : String,
    var userName : String

)