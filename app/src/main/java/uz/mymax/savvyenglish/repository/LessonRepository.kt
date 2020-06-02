package uz.mymax.savvyenglish.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.mymax.savvyenglish.exceptions.NoConnectivityException
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.SavvyApi
import uz.mymax.savvyenglish.network.toException
import uz.mymax.savvyenglish.utils.safeApiCall
import uz.mymax.savvyenglish.utils.safeApiCall2
import java.io.IOException

class LessonRepository constructor(
    private val api: SavvyApi
) {

    suspend fun fetchSortedTopics() = withContext(Dispatchers.IO) {
          safeApiCall2 { api.geTopicsAsync().await() }
    }

}