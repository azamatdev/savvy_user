package uz.mymax.savvyenglish.network.response


import com.squareup.moshi.Json

data class TopicTestResponse(
    @Json(name = "testCount")
    val testCount: Int = 0,
    @Json(name = "isFree")
    val isFree: Boolean = false,
    @Json(name = "price")
    val price: Int = 0,
    @Json(name = "paymentId")
    val paymentId: Int? = 0,
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "title")
    val title: String = ""
)




