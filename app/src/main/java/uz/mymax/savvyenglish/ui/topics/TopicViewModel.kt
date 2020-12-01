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
import java.io.Serializable


sealed class TopicEvent : Serializable {
    data class CreateTopic(val topic: TopicResponse) : TopicEvent()
    data class UpdateTopic(val topic: TopicResponse, val position: Int) : TopicEvent()
    data class DeleteTopic(val topicId: String, val position: Int) : TopicEvent()
    object GetTopics : TopicEvent()
}

class TopicViewModel constructor(private val repository: LessonRepository) : ViewModel() {
    val getTopicsState = MutableLiveData<NetworkState<List<TopicResponse>>>()
    val deleteTopicsState = MutableLiveData<NetworkState<String>>()
    val createTopicsState = MutableLiveData<NetworkState<TopicResponse>>()
    val updateTopicsState = MutableLiveData<NetworkState<TopicResponse>>()

    val subtopicResource = MutableLiveData<NetworkState<List<SubtopicResponse>>>()
    val explanationResource = MutableLiveData<NetworkState<List<ExplanationResponse>>>()

    fun setTopicState(topicEvent: TopicEvent) {
        viewModelScope.launch {
            repository.topicCalls(topicEvent)
                .onEach {
                    when (topicEvent) {
                        is TopicEvent.CreateTopic -> {
                            createTopicsState.value = it as NetworkState<TopicResponse>
                        }
                        is TopicEvent.UpdateTopic -> {
                            updateTopicsState.value = it as NetworkState<TopicResponse>
                        }
                        is TopicEvent.DeleteTopic -> {
                            if (it is NetworkState.Success)
                                deleteTopicsState.value =
                                    NetworkState.Success(topicEvent.position.toString())
                            else
                                deleteTopicsState.value =
                                    it as NetworkState<String>

                        }
                        is TopicEvent.GetTopics -> {
                            getTopicsState.value = it as NetworkState<List<TopicResponse>>
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

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
                    getTopicsState.value = it
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