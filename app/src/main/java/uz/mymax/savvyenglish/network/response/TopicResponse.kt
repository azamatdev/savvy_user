package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep


@Keep
data class TopicResponse(
    val id: Int,
    val title: String,
    val subtopicCount: Int
)