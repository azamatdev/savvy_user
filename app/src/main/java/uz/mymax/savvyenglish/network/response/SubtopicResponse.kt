package uz.mymax.savvyenglish.network.response



data class SubtopicResponse(
    val id: Int,
    val title: String,
    val description: String,
    val parentId: Int
)