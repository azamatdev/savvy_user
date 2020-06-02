package uz.mymax.savvyenglish.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import uz.mymax.savvyenglish.network.response.TopicResponse

interface SavvyApi {

    @GET("topic/sorted")
    fun geTopicsAsync() : Deferred<Response<List<TopicResponse>>>
}