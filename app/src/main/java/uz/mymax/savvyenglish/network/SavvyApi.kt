package uz.mymax.savvyenglish.network

import retrofit2.Response
import retrofit2.http.*
import uz.mymax.savvyenglish.network.dto.*
import uz.mymax.savvyenglish.network.response.*

interface SavvyApi {

    @POST("signup")
    suspend fun signUp(@Body registerDto: RegisterDto): Response<String>

    @POST("login")
    suspend fun login(@Body loginDto: LoginDto): Response<AuthResponse>

    //region  Topic
    @GET(TOPIC)
    suspend fun geTopics(): Response<List<TopicResponse>>

    @POST(TOPIC)
    suspend fun createTopic(@Body createTopic: TopicResponse): Response<TopicResponse>

    @PUT(TOPIC)
    suspend fun updateTopic(@Body updateTopic: TopicResponse): Response<TopicResponse>

    @DELETE("$TOPIC{id}")
    suspend fun deleteTopic(@Path("id") topicId: String): Response<String>
    //endregion

    //region Subtopic
    @GET("$SUBTOPIC_PARENT{id}")
    suspend fun getSubtopicsOfTopic(@Path("id") topicId: String): Response<List<SubtopicResponse>>

    @POST(SUBTOPIC)
    suspend fun createSubtopic(@Body subtopic: SubtopicResponse): Response<SubtopicResponse>

    @POST(SUBTOPIC)
    suspend fun updateSubtopic(@Body subtopic: SubtopicResponse): Response<SubtopicResponse>

    @DELETE("$SUBTOPIC{id}")
    suspend fun deleteSubtopic(@Path("id") subtopicId: String): Response<String>

    //region Explanations
    @GET("$EXPLANATION_PARENT{id}")
    suspend fun getExplanationsOfTopic(@Path("id") topicId: String): Response<List<ExplanationResponse>>

    @POST(EXPLANATION)
    suspend fun createExplanation(@Body explanation: ExplanationResponse): Response<ExplanationResponse>

    @POST(EXPLANATION)
    suspend fun updateSubtopic(@Body explanation: ExplanationResponse): Response<ExplanationResponse>

    @DELETE("$EXPLANATION{id}")
    suspend fun deleteExplanation(@Path("id") explanationId: String): Response<String>
    //endregion

    //region Themes
    @DELETE("$THEME{themeId}")
    suspend fun deleteTheme(@Path("themeId") themeId: String): Response<String>

    @GET("$THEME{themeId}")
    suspend fun getOneTheme(@Path("themeId") themeId: String): Response<ThemeTestResponse>

    @GET(THEME)
    suspend fun getAllThemes(): Response<List<ThemeTestResponse>>

    @POST(THEME)
    suspend fun createTheme(@Body test: CreateTestDto): Response<ThemeTestResponse>

    @PUT(THEME)
    suspend fun updateTheme(@Body theme: ThemeUpdateDto): Response<ThemeTestResponse>

    @GET("$TEST$THEME{themeId}")
    suspend fun getTestsOfTheme(@Path("themeId") themeId: String): Response<List<ThemeTestResponse>>

    @POST("$THEME{themeId}")
    suspend fun addTestToTheme(
        @Path("themeId") themeId: String,
        @Body testIdsHashMap: AddTestToThemeDto
    ): Response<String>

    //endregion

    //region Test
    @GET(TEST + "tests")
    suspend fun getVariantTests(): Response<List<VariantTestResponse>>

    @POST(TEST)
    suspend fun createTest(@Body test: CreateTestDto): Response<VariantTestResponse>

    @DELETE("$TEST{testId}")
    suspend fun deleteTest(@Path("testId") testId: String): Response<String>

    @PUT(TEST)
    suspend fun updateTest(@Body updatedTest: VariantTestResponse): Response<VariantTestResponse>

    //endregion

    //region Question
    @GET(QUESTION)
    suspend fun getAllQuestions(): Response<List<QuestionResponse>>

    @GET("$TEST$QUESTION{testId}")
    suspend fun getQuestionsOfTest(@Path("testId") testId: String): Response<List<QuestionResponse>>

    @POST(QUESTION)
    suspend fun createQuestion(@Body question: QuestionResponse): Response<QuestionResponse>

    @POST("$TEST{testId}")
    suspend fun addQuestionToTest(
        @Path("testId") testId: String,
        @Body questionIds: AddQuestionToTestDto
    ): Response<String>

    @DELETE("$QUESTION{questionId}")
    suspend fun deleteQuestion(@Path("questionId") questionId: String): Response<String>

    @PUT(QUESTION)
    suspend fun updateQuestion(@Body question: QuestionResponse): Response<QuestionResponse>

    //endregion
    @POST(PAY)
    suspend fun createReceipt(
        @Query("isTheme") isTheme: Boolean,
        @Query("themeOrTestId") id: String
    ): Response<PaymentReceiptResponse>

    @POST(PAY_CHECK)
    suspend fun payCheck(
        @Path("id") id: String
    ): Response<String>

    @POST(PAY_SEND)
    suspend fun paySend(
        @Query("phoneNumber") phone: String,
        @Query("themeOrTestId") id: String
    ): Response<String>
    //region payme

    //

}