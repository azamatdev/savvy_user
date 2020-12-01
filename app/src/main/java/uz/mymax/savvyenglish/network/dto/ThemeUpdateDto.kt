package uz.mymax.savvyenglish.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ThemeUpdateDto(
    var id: Int = 0,
    var isFree: Boolean = true,
    var price: Int = 0,
    var title: String = ""
)