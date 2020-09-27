package uz.mymax.savvyenglish.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.safeApiCall

class LessonRepository constructor(
    private val api: SavvyApi
) {

    suspend fun login(loginDto: LoginDto) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.login(loginDto) })

    }

    suspend fun signUp(registerDto: RegisterDto) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.signUp(registerDto) })
    }

    suspend fun fetchTopics() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.geTopics() })
    }

    suspend fun fetchSubtopics(topicId : String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getSubtopics(topicId) })
    }

}