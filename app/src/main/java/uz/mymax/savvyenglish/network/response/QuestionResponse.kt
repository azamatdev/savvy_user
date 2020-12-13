package uz.mymax.savvyenglish.network.response

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Keep
@Parcelize
data class QuestionResponse(
    @Json(name = "ansC")
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
    var correctAns: String = "",
    var isChecked: Boolean = false,
    var checkedIdName: String = ""
) : Parcelable

class MockData {
    companion object {
        fun getQuestionList(): ArrayList<QuestionResponse> {
            val arraylist = ArrayList<QuestionResponse>()

            val questionResponse = QuestionResponse()
            questionResponse.ansA = "smth"
            questionResponse.ansB = "smth"
            questionResponse.ansC = "smth"
            questionResponse.ansD = "smth"
            questionResponse.text = "smthfsafs"
            questionResponse.title = "smthfsafsfdafsa"
            questionResponse.isChecked = true
            questionResponse.correctAns = "A"
            questionResponse.checkedIdName = "A"

            arraylist.add(questionResponse)

            val questionResponse2 = QuestionResponse()
            questionResponse2.ansA = "smth"
            questionResponse2.ansB = "smth"
            questionResponse2.ansC = "smth"
            questionResponse2.ansD = "smth"
            questionResponse2.text = "smthfsafs"
            questionResponse2.title = "smthfsafsfdafsa"
            questionResponse2.isChecked = true
            questionResponse2.correctAns = "B"
            questionResponse2.checkedIdName = "D"
            arraylist.add(questionResponse2)

            val questionResponse3 = QuestionResponse()
            questionResponse3.ansA = "smth"
            questionResponse3.ansB = "smth"
            questionResponse3.ansC = "smth"
            questionResponse3.ansD = "smth"
            questionResponse3.text = "smthfsafs"
            questionResponse3.title = "smthfsafsfdafsa"
            questionResponse3.isChecked = true
            questionResponse3.correctAns = "C"
            questionResponse3.checkedIdName = "C"
            arraylist.add(questionResponse3)

            return arraylist
        }

    }
}


