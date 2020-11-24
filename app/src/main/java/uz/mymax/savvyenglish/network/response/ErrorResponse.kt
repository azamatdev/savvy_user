package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import org.json.JSONObject

@Keep
data class ErrorResponse(
    val timestamp: String="",
    val status: Int =0,
    val error: String = "",
    val message: String = "",
    val path: String = "",
    val jsonResponse : JSONObject = JSONObject()
)