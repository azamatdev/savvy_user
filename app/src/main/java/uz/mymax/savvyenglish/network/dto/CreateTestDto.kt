package uz.mymax.savvyenglish.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateTestDto(
    var isFree: Boolean = true,
    var price: Int = 0,
    var title: String = "",
    var themeId : Int? = null
)