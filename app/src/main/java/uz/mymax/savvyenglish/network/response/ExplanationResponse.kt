package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep

@Keep
data class ExplanationResponse(
    var id: Int,
    var text: String,
    var parentId: Int
)