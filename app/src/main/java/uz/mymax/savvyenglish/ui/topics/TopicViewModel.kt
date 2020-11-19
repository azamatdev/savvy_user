package uz.mymax.savvyenglish.ui.topics

import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.ExplanationResponse
import uz.mymax.savvyenglish.network.response.SubtopicResponse
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.repository.LessonRepository

class TopicViewModel constructor(private val repository: LessonRepository) : ViewModel() {


    val topicsResource = MutableLiveData<NetworkState<List<TopicResponse>>>()
    val subtopicResource = MutableLiveData<NetworkState<List<SubtopicResponse>>>()
    val explanationResource = MutableLiveData<NetworkState<List<ExplanationResponse>>>()

    fun fetchSubtopics(topicId: String) {
        viewModelScope.launch {
            repository.fetchSubtopics(topicId)
                .onEach {
                    subtopicResource.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun fetchTopics() {
        viewModelScope.launch {
            repository.fetchTopics()
                .onEach {
                    topicsResource.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun fetchExplanations(subtopicId: String) {
        viewModelScope.launch {
            repository.fetchExplanations(subtopicId)
                .onEach {
                    explanationResource.value = it
                }.launchIn(viewModelScope)
        }
    }
}