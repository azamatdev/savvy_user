package uz.mymax.savvyenglish.network

import com.google.gson.JsonObject
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import uz.mymax.savvyenglish.network.dto.*
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


    @GET("api/question")
    suspend fun fetchAllQuestions(): Response<List<QuestionItem>>


}