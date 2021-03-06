package uz.mymax.savvyenglish.repository

import kotlinx.coroutines.flow.flow
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.dto.*
import uz.mymax.savvyenglish.network.response.ErrorResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.network.safeApiCall
import uz.mymax.savvyenglish.ui.question.QuestionEvent
import uz.mymax.savvyenglish.ui.topics.TopicEvent

class LessonRepository constructor(
    private val api: SavvyApi
) {

    suspend fun signUp(registerDto: RegisterDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.signUp(registerDto) })
    }

    suspend fun login(loginDto: LoginDto) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.login(loginDto) })

    }

    //region Topics
    suspend fun fetchTopics() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.geTopics() })
    }

    suspend fun topicCalls(event: TopicEvent) = flow {
        emit(NetworkState.Loading)
        when (event) {
            is TopicEvent.CreateTopic -> {
                emit(safeApiCall { api.createTopic(event.topic) })
            }
            is TopicEvent.UpdateTopic -> {
                val updateCall = safeApiCall { api.updateTopic(event.topic) }
                if (updateCall is NetworkState.Success)
                    updateCall.data.positionUpdated = event.position
                emit(updateCall)
            }
            is TopicEvent.DeleteTopic -> {
                emit(safeApiCall { api.deleteTopic(event.topicId) })
            }
            is TopicEvent.GetTopics -> {
                emit(safeApiCall { api.geTopics() })
            }
        }
    }

    //endregion

    suspend fun fetchSubtopics(topicId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getSubtopicsOfTopic(topicId) })
    }

    suspend fun fetchExplanations(subtopicId: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getExplanationsOfTopic(subtopicId) })
    }


    suspend fun fetchAllQuestion() = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.getAllQuestions() })
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

    //region Question
    suspend fun questionCalls(event: QuestionEvent) = flow {
        emit(NetworkState.Loading)
        when (event) {
            is QuestionEvent.GetQuestionsOfTest -> {
                emit(safeApiCall { api.getQuestionsOfTest(event.testId) })
            }
            is QuestionEvent.GetAllQuestions -> {
                emit(safeApiCall { api.getAllQuestions() })
            }
            is QuestionEvent.CreateQuestion -> {
                val createQuestionCall = safeApiCall { api.createQuestion(event.question) }
                if (createQuestionCall is NetworkState.Success) {
                    val questionIds = ArrayList<Int>()
                    questionIds.add(createQuestionCall.data.id)

                    val addQuestionToTestCall =
                        safeApiCall {
                            api.addQuestionToTest(
                                testId = event.testId,
                                questionIds = AddQuestionToTestDto(questionIds)
                            )
                        }

                    emit(addQuestionToTestCall)
                } else
                    emit(createQuestionCall)

            }
            is QuestionEvent.UpdateQuestion -> {
                emit(safeApiCall { api.updateQuestion(event.question) })
            }
            is QuestionEvent.DeleteQuestion -> {
                emit(safeApiCall { api.deleteQuestion(event.questionId) })
            }
        }
    }
    //endregion

    //region Payment
    suspend fun checkTopic(isTopic: Boolean, id: String) = flow {
        emit(NetworkState.Loading)
        emit(safeApiCall { api.payCheck(isTopic, id) })
    }

    suspend fun pay(isTopic: Boolean, isTheme: Boolean, id: String, phone: String) = flow {
        emit(NetworkState.Loading)
        val createReceiptApi = safeApiCall { api.createReceipt(isTheme, id) }

        if (createReceiptApi is NetworkState.Success) {
            val sendToPhone = safeApiCall { api.paySend(isTopic, phone, id) }
            if (sendToPhone is NetworkState.Success) {
                emit(sendToPhone)
            }
            if (sendToPhone is NetworkState.GenericError) {
                emit(NetworkState.GenericError(sendToPhone.errorResponse))
            } else if (sendToPhone is NetworkState.Error) {
                emit(NetworkState.Error(sendToPhone.exception))
            }
        } else if (createReceiptApi is NetworkState.GenericError) {
            emit(NetworkState.GenericError(createReceiptApi.errorResponse))
        } else if (createReceiptApi is NetworkState.Error) {
            emit(NetworkState.Error(createReceiptApi.exception))
        }
    }
    //endregion
}