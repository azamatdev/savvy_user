package uz.mymax.savvyenglish.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.data.saveToken
import uz.mymax.savvyenglish.data.setLoggedIn
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.dto.LoginDto
import uz.mymax.savvyenglish.utils.*

class LoginFragment : Fragment() {

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
                when (resource) {
                    is Resource.Loading -> {
                        changeUiState(true)
                        hideKeyboard()
                    }
                    is Resource.Success -> {
                        changeUiState(false)
                        requireContext().saveToken(resource.data.token)
                        requireContext().setLoggedIn(true)
//                        showSnackbar("Success:")
                        findNavController().navigate(R.id.action_navigation_login_to_navigation_topics)
                    }
                    is Resource.Error -> {
                        changeUiState(false)
                        resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                    is Resource.GenericError -> {
                        changeUiState(false)
                        showSnackbar(resource.errorResponse.status.toString() + ":" + resource.errorResponse.error)
                    }
                }
            }


        })

        signUpButton.setOnClickListener {
            findNavController().navigate(R.id.destSignUp)
        }

        loginButton.setOnClickListener {
            if (allFieldsFilled()) {
                authViewModel.loginUser(getLoginDto())
            } else {
                usernameInput.showErrorIfNotFilled()
                passwordInput.showErrorIfNotFilled()
            }
        }

        usernameInput.hideErrorIfFilled()
        passwordInput.hideErrorIfFilled()

        fbLogin.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_navigation_topics)
        }
    }

    private fun getLoginDto() =
        LoginDto(usernameInput.text.toString(), passwordInput.text.toString())

    private fun allFieldsFilled() =
        usernameInput.text.toString().isNotEmpty() and passwordInput.text.toString().isNotEmpty()

    private fun changeUiState(isLoading: Boolean) {
        if (isLoading) {
            progressIndicator.makeVisible()
            loginButton.isEnabled = false
        } else {
            progressIndicator.hideVisibility()
            loginButton.isEnabled = true
        }
    }

}
