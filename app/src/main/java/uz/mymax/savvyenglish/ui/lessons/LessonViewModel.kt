package uz.mymax.savvyenglish.ui.lessons

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.repository.LessonRepository

class LessonViewModel constructor(private val repository: LessonRepository) : ViewModel() {

    private val topicsLiveData = MutableLiveData<Unit>()

    val topicsResource : LiveData<Resource<List<TopicResponse>>> = Transformations.switchMap(topicsLiveData){
        liveData{
            emit(Resource.Loading)
            emit(repository.fetchSortedTopics())
        }
    }

    @MainThread
    fun fetchTopics(){
        topicsLiveData.value = Unit
    }
}