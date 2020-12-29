package uz.mymax.savvyenglish.ui.tests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.dto.CreateTestDto
import uz.mymax.savvyenglish.network.dto.ThemeUpdateDto
import uz.mymax.savvyenglish.network.response.ThemeTestResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.repository.LessonRepository
import uz.mymax.savvyenglish.utils.SingleEvent

sealed class ThemeEvent {
    object GetAllThemes : ThemeEvent()
    data class DeleteTheme(val themeId: String) : ThemeEvent()
    data class CreateTheme(val createTestDto: CreateTestDto) : ThemeEvent()
    data class UpdateTheme(val updateTheme: ThemeUpdateDto) : ThemeEvent()
    data class GetTestsOfTheme(val themeId: String) : ThemeEvent()
    data class CreateTestTheme(val theme: CreateTestDto) : ThemeEvent()
}

sealed class TestEvent {
    object GetAllTestsOfDTM : TestEvent()
    data class DeleteTest(val testId: String) : TestEvent()
    data class CreateTest(val createTestDto: CreateTestDto) : TestEvent()
    data class UpdateTest(val updateTest: VariantTestResponse) : TestEvent()
}

class TestViewModel(val repository: LessonRepository) : ViewModel() {


    val variantTestState = MutableLiveData<NetworkState<List<VariantTestResponse>>>()
    val createTestState = MutableLiveData<NetworkState<VariantTestResponse>>()
    val deleteTestState = MutableLiveData<NetworkState<String>>()
    val updateTestState = MutableLiveData<NetworkState<VariantTestResponse>>()

    val allThemeState = MutableLiveData<NetworkState<List<ThemeTestResponse>>>()
    val deleteThemeState = MutableLiveData<NetworkState<String>>()
    val createThemeState = MutableLiveData<NetworkState<ThemeTestResponse>>()
    val updateThemeState = MutableLiveData<NetworkState<ThemeTestResponse>>()
    val testsOfThemeState = MutableLiveData<NetworkState<List<ThemeTestResponse>>>()
    val createThemeTestState = MutableLiveData<NetworkState<String>>()

    val checkPayState = MutableLiveData<SingleEvent<NetworkState<String>>>()
    val payTopicState = MutableLiveData<SingleEvent<NetworkState<String>>>()

    fun setStateTheme(themeEvent: ThemeEvent) {
        viewModelScope.launch {
            when (themeEvent) {
                is ThemeEvent.GetAllThemes -> {
                    repository.getAllThemes()
                        .onEach {
                            allThemeState.value = it
                        }.launchIn(viewModelScope)
                }
                is ThemeEvent.DeleteTheme -> {
                    repository.deleteTheme(themeId = themeEvent.themeId)
                        .onEach {
                            deleteThemeState.value = it
                        }.launchIn(viewModelScope)
                }
                is ThemeEvent.CreateTheme -> {
                    repository.createTheme(themeEvent.createTestDto)
                        .onEach {
                            createThemeState.value = it
                        }.launchIn(viewModelScope)
                }
                is ThemeEvent.UpdateTheme -> {
                    repository.updateTheme(themeEvent.updateTheme)
                        .onEach {
                            updateThemeState.value = it
                        }.launchIn(viewModelScope)
                }
                is ThemeEvent.GetTestsOfTheme -> {
                    repository.getTestsOfTheme(themeEvent.themeId)
                        .onEach {
                            testsOfThemeState.value = it
                        }.launchIn(viewModelScope)
                }
                is ThemeEvent.CreateTestTheme -> {
                    repository.createThemeTest(themeEvent.theme)
                        .onEach {
                            createThemeTestState.value = it
                        }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun setStateTest(testEvent: TestEvent) {
        viewModelScope.launch {
            when (testEvent) {
                is TestEvent.GetAllTestsOfDTM -> {
                    repository.getVariantTests()
                        .onEach {
                            variantTestState.value = it
                        }.launchIn(viewModelScope)
                }
                is TestEvent.UpdateTest -> {
                    repository.updateTest(testEvent.updateTest)
                        .onEach {
                            updateTestState.value = it
                        }.launchIn(viewModelScope)
                }
                is TestEvent.DeleteTest -> {
                    repository.deleteTest(testEvent.testId)
                        .onEach {
                            deleteTestState.value = it
                        }.launchIn(viewModelScope)
                }
                is TestEvent.CreateTest -> {
                    repository.createTest(testEvent.createTestDto)
                        .onEach {
                            createTestState.value = it
                        }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun checkTopic(isTopic: Boolean, id: String) {
        viewModelScope.launch {
            repository.checkTopic(isTopic, id).onEach {
                checkPayState.value = SingleEvent(it)
            }.launchIn(viewModelScope)
        }
    }

    fun payToTopic(isTheme: Boolean, id: String, phone: String) {
        viewModelScope.launch {
            repository.pay(false,isTheme, id, phone).onEach {
                payTopicState.value = SingleEvent(it)
            }.launchIn(viewModelScope)
        }
    }

}