package uz.mymax.savvyenglish.ui.auth

import androidx.lifecycle.*
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.network.response.AuthResponse
import uz.mymax.savvyenglish.repository.LessonRepository
import uz.mymax.savvyenglish.utils.Event

class AuthViewModel constructor(private val repository: LessonRepository) : ViewModel(){

    private var loginLiveData = MutableLiveData<Any>()
    val loginResource : LiveData<Event<Resource<AuthResponse>>> = Transformations.switchMap(loginLiveData){
        liveData {
            emit(Event(Resource.Loading))
            emit(Event(repository.login(loginLiveData.value as LoginDto)))
        }
    }

    private var registerLiveData = MutableLiveData<Any>()
    val registerResource : LiveData<Event<Resource<AuthResponse>>> = Transformations.switchMap(registerLiveData){
        liveData {
            emit(Event(Resource.Loading))
            emit(Event(repository.signUp(registerLiveData.value as RegisterDto)))
        }
    }


    fun loginUser(loginDto: LoginDto){
        loginLiveData.postValue(loginDto)
    }

    fun registerUser(registerDto: RegisterDto){
        registerLiveData.postValue(registerDto)
    }
}