package uz.mymax.savvyenglish.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.utils.safeApiCall

class LessonRepository constructor(
    private val api: SavvyApi
) {

    suspend fun login(loginDto: LoginDto) = withContext(Dispatchers.IO) {
        safeApiCall { api.login(loginDto) }
    }

    suspend fun signUp(registerDto: RegisterDto) = withContext(Dispatchers.IO) {
        safeApiCall { api.signUp(registerDto) }
    }

    suspend fun fetchSortedTopics() = withContext(Dispatchers.IO) {
        safeApiCall { api.geTopics() }
    }


}