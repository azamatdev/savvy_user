package uz.mymax.savvyenglish.repository

import kotlinx.coroutines.flow.flow
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.safeApiCall

class LessonRepository constructor(
    private val api: SavvyApi
) {

    suspend fun login(loginDto: LoginDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.login(loginDto) })

    }

    suspend fun signUp(registerDto: RegisterDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.signUp(registerDto) })
    }

    suspend fun fetchTopics() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.geTopics() })
    }

    suspend fun fetchSubtopics(topicId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getSubtopics(topicId) })
    }

    suspend fun fetchExplanations(subtopicId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getExplanations(subtopicId) })
    }

    suspend fun fetchTopicTests() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.fetchTopicTests() })
    }

    suspend fun fetchVariantTests() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.fetchVariantTests() })
    }

}