package uz.mymax.savvyenglish.network.dto

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddQuestionToTestDto(
    val questionIds: List<Int>
)