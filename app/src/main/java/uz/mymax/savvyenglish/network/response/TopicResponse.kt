package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep


@Keep
data class TopicResponse(
    val id: Int = 0,
    val title: String = "",
    val subtopicCount: Int = 0
) {
    @Transient
    var positionUpdated = -1
}