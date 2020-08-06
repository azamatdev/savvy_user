package uz.mymax.savvyenglish.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.ui.base.BaseFragment
import uz.mymax.savvyenglish.utils.PrefManager
import uz.mymax.savvyenglish.utils.hideErrorIfFilled
import uz.mymax.savvyenglish.utils.hideKeyboard
import uz.mymax.savvyenglish.utils.showErrorIfNotFilled


class SignUpFragment : BaseFragment() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.registerResource.observe(viewLifecycleOwner, Observer {
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
                        findNavController().navigate(R.id.action_navigation_signup_to_navigation_topics)
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

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }


        btn_sign_up.setOnClickListener {
            if(allFieldFilled()){
                if(passwordConfirmed()){
                    authViewModel.registerUser(getRegisterDto())
                }
                else{
                    input_confirm_password.error = "Password does not match!"
                }
            }
            else{
                input_full_name.showErrorIfNotFilled()
                input_username.showErrorIfNotFilled()
                input_email.showErrorIfNotFilled()
                input_password.showErrorIfNotFilled()
                input_confirm_password.showErrorIfNotFilled()
            }
        }

        input_full_name.hideErrorIfFilled()
        input_username.hideErrorIfFilled()
        input_email.hideErrorIfFilled()
        input_password.hideErrorIfFilled()
        input_confirm_password.hideErrorIfFilled()
    }

    private fun passwordConfirmed() = input_password.text.toString().equals(input_confirm_password.text.toString())

    private fun allFieldFilled() =
        input_full_name.text.toString().isNotEmpty() and
                input_username.text.toString().isNotEmpty() and
                input_email.text.toString().isNotEmpty() and
                input_password.text.toString().isNotEmpty() and
                input_confirm_password.text.toString().isNotEmpty()


    private fun getRegisterDto() = RegisterDto(
        name = input_full_name.text.toString(),
        userName = input_username.text.toString(),
        email = input_email.text.toString(),
        phone = input_phone_number.text.toString().replace(" ", ""),
        password = input_password.text.toString()
    )
}
