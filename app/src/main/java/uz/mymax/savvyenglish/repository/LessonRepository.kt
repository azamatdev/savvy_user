package uz.mymax.savvyenglish.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.response.ErrorResponse
import uz.mymax.savvyenglish.utils.safeApiCall2

class LessonRepository constructor(
    var errorConverter: Converter<ResponseBody, ErrorResponse>,
    private val api: SavvyApi
) {

    suspend fun login(loginDto: LoginDto) = withContext(Dispatchers.IO) {
        safeApiCall2(errorConverter) { api.loginAsync(loginDto).await() }
    }

    suspend fun signUp(registerDto: RegisterDto) = withContext(Dispatchers.IO) {
        safeApiCall2(errorConverter) { api.signUpAsync(registerDto).await() }
    }

    suspend fun fetchSortedTopics() = withContext(Dispatchers.IO) {
        safeApiCall2(errorConverter) { api.geTopicsAsync().await() }
    }


}