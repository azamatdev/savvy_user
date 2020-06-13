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
    fun signUpAsync(@Body registerDto: RegisterDto) : Deferred<Response<AuthResponse>>

    @POST("auth/login")
    fun loginAsync(@Body loginDto: LoginDto) : Deferred<Response<AuthResponse>>


    @GET("topic/sorted")
    fun geTopicsAsync() : Deferred<Response<List<TopicResponse>>>
}