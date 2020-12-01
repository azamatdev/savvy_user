package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class SubtopicResponse(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val parentId: Int = 0
)