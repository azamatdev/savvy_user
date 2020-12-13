package uz.mymax.savvyenglish.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.dto.RegisterDto
import uz.mymax.savvyenglish.utils.*


class SignUpFragment : Fragment() {

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
                when (resource) {
                    is NetworkState.Loading -> {
                        changeUiStateEnabled(true, progressBar, signUpButton)
                    }
                    is NetworkState.Success -> {
                        changeUiStateEnabled(false, progressBar, signUpButton)
                        findNavController().navigate(R.id.action_navigation_signup_to_navigation_topics)
                    }
                    is NetworkState.Error -> {
                        changeUiStateEnabled(false, progressBar, signUpButton)
                        showSnackbar(resource.exception.message.toString())
                    }
                    is NetworkState.GenericError -> {
                        changeUiStateEnabled(false, progressBar, signUpButton)
                        showSnackbar(resource.errorResponse.message)
                    }
                }
            }
        })

        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }


        signUpButton.setOnClickListener {
            if (allFieldFilled()) {
                if (passwordConfirmed()) {
                    hideKeyboard()
                    authViewModel.registerUser(getRegisterDto())
                } else {
                    input_confirm_password.error = "Password does not match!"
                }
            } else {
                input_full_name.showErrorIfNotFilled()
                usernameInput.showErrorIfNotFilled()
                input_email.showErrorIfNotFilled()
                passwordInput.showErrorIfNotFilled()
                input_confirm_password.showErrorIfNotFilled()
            }
        }

        input_full_name.hideErrorIfFilled()
        usernameInput.hideErrorIfFilled()
        input_email.hideErrorIfFilled()
        passwordInput.hideErrorIfFilled()
        input_confirm_password.hideErrorIfFilled()
    }

    private fun passwordConfirmed() =
        passwordInput.text.toString().equals(input_confirm_password.text.toString())

    private fun allFieldFilled() =
        input_full_name.text.toString().isNotEmpty() and
                usernameInput.text.toString().isNotEmpty() and
                input_email.text.toString().isNotEmpty() and
                passwordInput.text.toString().isNotEmpty() and
                input_confirm_password.text.toString().isNotEmpty()


    private fun getRegisterDto() = RegisterDto(
        name = input_full_name.text.toString(),
        userName = usernameInput.text.toString(),
        email = input_email.text.toString(),
        phone = input_phone_number.text.toString().replace(" ", ""),
        password = passwordInput.text.toString()
    )
}
