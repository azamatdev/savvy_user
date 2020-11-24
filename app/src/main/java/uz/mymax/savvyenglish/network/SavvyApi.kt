package uz.mymax.savvyenglish.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.response.*

interface SavvyApi {

    @POST("auth/users")
    suspend fun signUp(@Body registerDto: RegisterDto): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body loginDto: LoginDto): Response<AuthResponse>

    @GET("api/topics")
    suspend fun geTopics(): Response<List<TopicResponse>>

    @GET("api/subtopics/parent/{id}")
    suspend fun getSubtopics(@Path("id") topicId: String): Response<List<SubtopicResponse>>

    @GET("api/explanations/parent/{id}")
    suspend fun getExplanations(@Path("id") topicId: String): Response<List<ExplanationResponse>>

    @GET("api/themes")
    suspend fun fetchTopicTests(): Response<List<TopicTestResponse>>

    @GET("api/test/tests")
    suspend fun fetchVariantTests(): Response<List<VariantTestResponse>>

    @GET("api/question")
    suspend fun fetchAllQuestions(): Response<List<QuestionItem>>


}