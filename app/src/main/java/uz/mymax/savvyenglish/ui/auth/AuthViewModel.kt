package uz.mymax.savvyenglish.ui.auth

import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.response.AuthResponse
import uz.mymax.savvyenglish.repository.LessonRepository
import uz.mymax.savvyenglish.utils.SingleEvent

class AuthViewModel constructor(private val repository: LessonRepository) : ViewModel() {

    val loginResource = MutableLiveData<SingleEvent<NetworkState<AuthResponse>>>()
    val registerResource = MutableLiveData<SingleEvent<NetworkState<Any>>>()

    fun loginUser(loginDto: LoginDto) {
        viewModelScope.launch {
            repository.login(loginDto)
                .onEach {
                    loginResource.value = SingleEvent(it)
                }
                .launchIn(viewModelScope)
        }
    }

    fun registerUser(registerDto: RegisterDto) {
        viewModelScope.launch {
            repository.signUp(registerDto)
                .onEach {
                    registerResource.value = SingleEvent(it)
                }
                .launchIn(viewModelScope)
        }
    }
}