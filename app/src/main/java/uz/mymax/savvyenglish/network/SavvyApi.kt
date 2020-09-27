package uz.mymax.savvyenglish.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.response.AuthResponse
import uz.mymax.savvyenglish.network.response.SubtopicResponse
import uz.mymax.savvyenglish.network.response.TopicResponse

interface SavvyApi {

    @POST("auth/users")
    suspend fun signUp(@Body registerDto: RegisterDto): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body loginDto: LoginDto): Response<AuthResponse>

    @GET("api/topics")
    suspend fun geTopics(): Response<List<TopicResponse>>

    @GET("api/subtopics/parent/{id}")
    suspend fun getSubtopics(@Path("id") topicId: String): Response<List<SubtopicResponse>>


}