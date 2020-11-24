package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep


@Keep
data class SubtopicResponse(
    val id: Int,
    val title: String,
    val description: String,
    val parentId: Int
)