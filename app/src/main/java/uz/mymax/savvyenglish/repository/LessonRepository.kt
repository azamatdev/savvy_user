package uz.mymax.savvyenglish.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.dto.*
import uz.mymax.savvyenglish.network.response.ErrorResponse
import uz.mymax.savvyenglish.network.response.ThemeTestResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse
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


    suspend fun fetchAllQuestion() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.fetchAllQuestions() })
    }

    //region Tests CRUD
    suspend fun getVariantTests() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getVariantTests() })
    }

    suspend fun createTest(createTestDto: CreateTestDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.createTest(createTestDto) })
    }

    suspend fun deleteTest(testId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.deleteTest(testId) })
    }

    suspend fun updateTest(updatedTest: VariantTestResponse) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.updateTest(updatedTest) })
    }

    //endregion

    //region Theme CRUD
    suspend fun getAllThemes() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getAllThemes() })
    }

    suspend fun getOneTheme(themeId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getOneTheme(themeId) })
    }

    suspend fun updateTheme(themeUpdate: ThemeUpdateDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.updateTheme(themeUpdate) })
    }

    suspend fun deleteTheme(themeId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.deleteTheme(themeId) })
    }

    suspend fun createTheme(createTestDto: CreateTestDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.createTheme(createTestDto) })
    }

    suspend fun getTestsOfTheme(themeId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getTestsOfTheme(themeId) })
    }

    suspend fun createThemeTest(createTestDto: CreateTestDto) = flow {
        emit(NetworkState.Loading)

        //Create Test
        val createTestApi = safeApiCall {
            api.createTest(
                createTestDto
            )
        }

        //Return if Error
        if (createTestApi is NetworkState.Error) {
            emit(NetworkState.Error(createTestApi.exception))
            return@flow
        }
        if (createTestApi is NetworkState.GenericError) {
            emit(NetworkState.GenericError(createTestApi.errorResponse))
            return@flow
        }

        //Added newly added test to Theme
        if (createTestApi is NetworkState.Success) {

            val questionIdList = ArrayList<Int>()
            questionIdList.add(createTestApi.data.id)
            val addingRequest = AddTestToThemeDto(questionIdList)
            val addTestToThemeCall = safeApiCall {
                api.addTestToTheme(
                    createTestDto.themeId.toString(),
                    addingRequest
                )
            }
            emit(addTestToThemeCall)
        }

    }
    //endregion
}