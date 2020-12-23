package uz.mymax.savvyenglish.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.mymax.savvyenglish.utils.SingleEvent

class SharedViewModel() : ViewModel() {

    private val _timerLiveData = MutableLiveData<SingleEvent<String>>()
    var timerLiveData: LiveData<SingleEvent<String>> = _timerLiveData

    fun setTime(time: String) {
        _timerLiveData.value = SingleEvent(time)
    }
}