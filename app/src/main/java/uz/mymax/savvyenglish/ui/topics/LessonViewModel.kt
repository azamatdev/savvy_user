package uz.mymax.savvyenglish.ui.topics

import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.repository.LessonRepository
import uz.mymax.savvyenglish.utils.Event

class LessonViewModel constructor(private val repository: LessonRepository) : ViewModel() {


    val topicsResource = MutableLiveData<Resource<List<TopicResponse>>>()
    val subtopicResource = MutableLiveData<Resource<List<TopicResponse>>>()

    fun fetchSubtopics() {
        viewModelScope.launch {
            repository.fetchSubtopics()
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
}