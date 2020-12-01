package uz.mymax.savvyenglish.ui.question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.repository.LessonRepository

sealed class QuestionEvent() {
    object GetAllQuestions : QuestionEvent()
    data class GetQuestionsOfTest(val testId: String) : QuestionEvent()
    data class CreateQuestion(val question: QuestionResponse, val testId: String) : QuestionEvent()
    data class UpdateQuestion(val question: QuestionResponse) : QuestionEvent()
    data class DeleteQuestion(val questionId: String, val position: Int) : QuestionEvent()
}

class QuestionSetViewModel(val repository: LessonRepository) : ViewModel() {
    val allQuestionsState = MutableLiveData<NetworkState<List<QuestionResponse>>>()
    val questionsOfTestState = MutableLiveData<NetworkState<List<QuestionResponse>>>()
    val deleteQuestionState = MutableLiveData<NetworkState<String>>()
    val updateQuestionState = MutableLiveData<NetworkState<QuestionResponse>>()
    val createQuestionState = MutableLiveData<NetworkState<String>>()

    fun setStateQuestion(questionEvent: QuestionEvent) {
        viewModelScope.launch {
            repository.questionCalls(questionEvent)
                .onEach {
                    when (questionEvent) {
                        is QuestionEvent.GetQuestionsOfTest -> {
                            questionsOfTestState.value = it as NetworkState<List<QuestionResponse>>
                        }
                        is QuestionEvent.GetAllQuestions -> {
                            allQuestionsState.value = it as NetworkState<List<QuestionResponse>>
                        }
                        is QuestionEvent.CreateQuestion -> {
                            createQuestionState.value = it as NetworkState<String>
                        }
                        is QuestionEvent.UpdateQuestion -> {
                            updateQuestionState.value = it as NetworkState<QuestionResponse>
                        }
                        is QuestionEvent.DeleteQuestion -> {
                            if (it is NetworkState.Success)
                                deleteQuestionState.value =
                                    NetworkState.Success(questionEvent.position.toString())
                            else
                                deleteQuestionState.value = it as NetworkState<String>
                        }
                    }
                }.launchIn(viewModelScope)

        }
    }

}