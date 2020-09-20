package uz.mymax.savvyenglish.ui.base

import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import uz.mymax.savvyenglish.MainActivity
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.exceptions.EmptyBodyException
import uz.mymax.savvyenglish.exceptions.NoConnectivityException
import uz.mymax.savvyenglish.utils.*
import java.io.IOException

abstract class BaseFragment() : Fragment() {


    protected fun showSnackbar(message : String) =
        Snackbar.make(requireView(),message , Snackbar.LENGTH_SHORT).show()

    fun handleErrors(exception: Exception) {
        when (exception) {
            is NoConnectivityException -> showSnackbar(getString(R.string.error_not_connected))
            is EmptyBodyException -> showSnackbar(getString(R.string.error_generic))
            is IOException -> showSnackbar(exception.message ?: "Something is wrong")

        }
    }
    fun showLoading(){
        if(context is MainActivity){
            (context as MainActivity).findViewById<RelativeLayout>(R.id.loader_layout).makeVisible()
        }
    }
    fun hideLoading(){
        if(context is MainActivity){
            (context as MainActivity).findViewById<RelativeLayout>(R.id.loader_layout).hideVisibility()
        }
    }


}