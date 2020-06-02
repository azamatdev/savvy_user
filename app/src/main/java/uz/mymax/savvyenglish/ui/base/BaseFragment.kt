package uz.mymax.savvyenglish.ui.base

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.exceptions.EmptyBodyException
import uz.mymax.savvyenglish.exceptions.NoConnectivityException
import java.io.IOException

abstract class BaseFragment() : Fragment() {


    protected fun acknowledgeGenericError() =
        Snackbar.make(requireView(), R.string.error_generic, Snackbar.LENGTH_SHORT).show()

    protected fun acknowledgeConnectionError() =
        Snackbar.make(requireView(), R.string.error_not_connected, Snackbar.LENGTH_SHORT).show()

    protected fun acknowledgeError(message: String) =
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()

    fun handleErrors(exception: Exception) {
        when (exception) {
            is NoConnectivityException -> acknowledgeConnectionError()
            is EmptyBodyException -> acknowledgeGenericError()
            is IOException -> acknowledgeError(exception.message ?: "Something is wrong")

        }
    }
//    protected fun acknowledgeResourceError(error: Resource.Error) {
//        if (!error.isConnected) {
//            acknowledgeConnectionError()
//        } else {
//            error.exception.message?.let { acknowledgeError(it) } ?: acknowledgeGenericError()
//        }
//    }
}