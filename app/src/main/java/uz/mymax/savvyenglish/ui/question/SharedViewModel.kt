package uz.mymax.savvyenglish.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.mymax.savvyenglish.utils.Event

class SharedViewModel() : ViewModel() {

    private val _timerLiveData = MutableLiveData<Event<String>>()
    var timerLiveData: LiveData<Event<String>> = _timerLiveData

    fun setTime(time: String) {
        _timerLiveData.value = Event(time)
    }
}