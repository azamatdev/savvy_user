package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import org.json.JSONObject

data class ErrorResponse(
    val timestamp: String="",
    var status: Int =0,
    var error: String = "",
    var message: String = "",
    val path: String = ""
){
    var jsonResponse : JSONObject = JSONObject()
}