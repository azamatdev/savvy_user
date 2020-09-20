package uz.mymax.savvyenglish.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.response.AuthResponse
import uz.mymax.savvyenglish.network.response.TopicResponse

interface SavvyApi {

    @POST("auth/users")
    suspend fun signUp(@Body registerDto: RegisterDto): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body loginDto: LoginDto): Response<AuthResponse>


    @GET("topic/sorted")
    suspend fun geTopics(): Response<List<TopicResponse>>
}