package uz.mymax.savvyenglish.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class QuestionResponse(@Json(name = "ansC")
                        var ansC: String = "",
                            @Json(name = "ansB")
                        var ansB: String = "",
                            @Json(name = "ansD")
                        var ansD: String = "",
                            @Json(name = "ansA")
                        var ansA: String = "",
                            @Json(name = "base64")
                        var base: String = "",
                            @Json(name = "id")
                        var id: Int = 0,
                            @Json(name = "text")
                        var text: String = "",
                            @Json(name = "title")
                        var title: String = "",
                            @Json(name = "correctAns")
                        var correctAns: String = "") : Serializable {
    var isChecked = false
    var checkedId = -1213
}