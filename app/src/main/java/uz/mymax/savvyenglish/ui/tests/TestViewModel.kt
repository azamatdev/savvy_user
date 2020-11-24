package uz.mymax.savvyenglish.ui.tests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.QuestionItem
import uz.mymax.savvyenglish.network.response.TopicTestResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.repository.LessonRepository

class TestViewModel(val repository: LessonRepository) : ViewModel() {

    val testSetState = MutableLiveData<NetworkState<List<QuestionItem>>>()
    val topicTestState = MutableLiveData<NetworkState<List<TopicTestResponse>>>()
    val variantTestState = MutableLiveData<NetworkState<List<VariantTestResponse>>>()

    fun fetchTestSet() {
        viewModelScope.launch {
            repository.fetchAllQuestion()
                .onEach {
                    testSetState.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun fetchTopicTests() {
        viewModelScope.launch {
            repository.fetchTopicTests()
                .onEach {
                    topicTestState.value = it
                }.launchIn(viewModelScope)
        }
    }

    fun fetchVariantTests() {
        viewModelScope.launch {
            repository.fetchVariantTests()
                .onEach {
                    variantTestState.value = it
                }.launchIn(viewModelScope)
        }
    }
}