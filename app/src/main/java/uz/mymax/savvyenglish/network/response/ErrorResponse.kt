package uz.mymax.savvyenglish.network.response

import org.json.JSONObject

data class ErrorResponse(
    val timestamp: String="",
    val status: Int =0,
    val error: String = "",
    val message: String = "",
    val path: String = "",
    val jsonResponse : JSONObject = JSONObject()
)