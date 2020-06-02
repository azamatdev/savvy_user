package uz.mymax.savvyenglish.network.response

data class TopicResponse(
    val partTitle : String,
    val topicList : List<Topic>
)

data class Topic(
    val id : Int,
    val title :String
)