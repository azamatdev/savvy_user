package uz.mymax.savvyenglish.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.btn_sign_up
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.ui.base.BaseFragment
import uz.mymax.savvyenglish.utils.*

class LoginFragment : BaseFragment() {

    private val authViewModel: AuthViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.loginResource.observe(viewLifecycleOwner, Observer {
           it.getContentIfNotHandled()?.let { resource ->
               when(resource){
                   is Resource.Loading -> {
                       Log.d("LiveDataTag", "Loading")
                       hideKeyboard()
                       showLoading()
                   }
                   is Resource.Success -> {
                       hideLoading()
                       PrefManager.saveToken(requireContext(), resource.data.authToken)
                   }
                   is Resource.Error -> {
                       hideLoading()
                       handleErrors(exception = resource.exception)
                   }
                   is Resource.GenericError -> {
                       hideLoading()
                       showSnackbar(resource.errorResponse.message)
                   }
               }
           }


        })

        btn_sign_up.setOnClickListener {
            findNavController().navigate(R.id.navigation_signup)
        }

        btn_login.setOnClickListener {
            if (allFieldsFilled()) {
                authViewModel.loginUser(getLoginDto())
            } else {
                input_username.showErrorIfNotFilled()
                input_password.showErrorIfNotFilled()
            }
        }

        input_username.hideErrorIfFilled()
        input_password.hideErrorIfFilled()
    }

    private fun getLoginDto() =
        LoginDto(input_username.text.toString(), input_password.text.toString())

    private fun allFieldsFilled() =
        input_username.text.toString().isNotEmpty() and input_password.text.toString().isNotEmpty()

}
