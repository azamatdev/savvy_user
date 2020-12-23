package uz.mymax.savvyenglish.ui.tests.admin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_add_theme.*
import kotlinx.android.synthetic.main.dialog_add_theme.input_phone_number
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.ext.android.inject
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.dto.CreateTestDto
import uz.mymax.savvyenglish.network.dto.ThemeUpdateDto
import uz.mymax.savvyenglish.network.response.ThemeTestResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.ui.tests.TestEvent
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.tests.ThemeEvent
import uz.mymax.savvyenglish.utils.*
import java.io.Serializable


sealed class DialogEvent : Serializable {
    object CreateTheme : DialogEvent()
    data class CreateThemeTest(val themeId: String) : DialogEvent()
    data class UpdateTheme(val theme: ThemeTestResponse) : DialogEvent()
    object CreateTest : DialogEvent()
    data class UpdateTest(val variant: VariantTestResponse) : DialogEvent()
    data class PayToTest(val isTheme: Boolean, val id: String) : DialogEvent()
}

class AddTestDialog : DialogFragment() {

    lateinit var addCLick: ((Boolean) -> Unit)
    private val viewModel: TestViewModel by inject()
    private var isUpdating: Boolean = false
    private var theme: ThemeTestResponse? = null
    override fun onResume() {
        super.onResume()
        if (dialog != null) {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val inset = InsetDrawable(ColorDrawable(Color.TRANSPARENT), 20)
            dialog?.getWindow()?.setBackgroundDrawable(inset);
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return activity?.layoutInflater?.inflate(
            R.layout.dialog_add_theme,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val event = it.getSerializable("event") as DialogEvent
            when (event) {
                is DialogEvent.UpdateTheme -> {
                    themeTitleInput.setText(event.theme.title)
                    paidCheckbox.isChecked = !event.theme.isFree
                    if (paidCheckbox.isChecked) {
                        labelPrice.visible()
                        priceTestInput.visible()
                        priceTestInput.setText(event.theme.price.toString())
                    } else {
                        labelPrice.gone()
                        priceTestInput.gone()
                    }
                    btnAddTest.text = "Update"
                }
                is DialogEvent.UpdateTest -> {
                    themeTitleInput.setText(event.variant.title)
                    paidCheckbox.isChecked = !event.variant.isFree
                    if (paidCheckbox.isChecked) {
                        labelPrice.visible()
                        priceTestInput.visible()
                        priceTestInput.setText(event.variant.price.toString())
                    } else {
                        labelPrice.gone()
                        priceTestInput.gone()
                    }
                    btnAddTest.text = "Update"
                }
                is DialogEvent.PayToTest -> {
                    labelTitle.text = "Phone"
                    input_phone_number.visible()
                    themeTitleInput.gone()
                    paidCheckbox.gone()
                    btnAddTest.text = "Pay"
                }
                else -> null
            }
        }

        themeTitleInput.hideErrorIfFilled()
        priceTestInput.hideErrorIfFilled()

        paidCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                labelPrice.visible()
                priceTestInput.visible()
            } else {
                labelPrice.gone()
                priceTestInput.gone()
            }
        }
        btnAddTest.setOnClickListener {
            val event = arguments?.getSerializable("event") as DialogEvent
            if (event is DialogEvent.PayToTest) {
                if (input_phone_number.text.toString().replace(" ", "").length < 9) {
                    input_phone_number.showErrorIfNotFilled()
                    return@setOnClickListener
                }

            } else {
                if (themeTitleInput.text.toString().isEmpty()) {
                    themeTitleInput.showErrorIfNotFilled()
                    return@setOnClickListener
                }
                if (paidCheckbox.isChecked) {
                    if (priceTestInput.text.toString().isEmpty()) {
                        priceTestInput.showErrorIfNotFilled()
                        return@setOnClickListener
                    }
                }

            }

            val createDto = CreateTestDto()
            createDto.title = themeTitleInput.text.toString()
            createDto.isFree = !paidCheckbox.isChecked
            if (paidCheckbox.isChecked)
                createDto.price = priceTestInput.text.toString().toInt()

            when (event) {
                is DialogEvent.CreateThemeTest -> {
                    createDto.themeId = event.themeId.toInt()
                    viewModel.setStateTheme(ThemeEvent.CreateTestTheme(createDto))
                }
                is DialogEvent.CreateTest -> {
                    viewModel.setStateTest(TestEvent.CreateTest(createDto))
                }
                is DialogEvent.PayToTest -> {
                    viewModel.payToTopic(
                        event.isTheme,
                        event.id,
                        "998" + input_phone_number.text.toString().replace(" ", "")
                    )
                }
                is DialogEvent.UpdateTest -> {
                    val updatedTest = event.variant
                    updatedTest.title = createDto.title
                    updatedTest.isFree = createDto.isFree
                    updatedTest.price = createDto.price
                    viewModel.setStateTest(TestEvent.UpdateTest(updatedTest))
                }
                is DialogEvent.CreateTheme -> {
                    viewModel.setStateTheme(ThemeEvent.CreateTheme(createDto))
                }
                is DialogEvent.UpdateTheme -> {
                    val updateTheme = ThemeUpdateDto(
                        id = event.theme.id,
                        isFree = createDto.isFree,
                        price = createDto.price,
                        title = createDto.title
                    )
                    viewModel.setStateTheme(ThemeEvent.UpdateTheme(updateTheme))
                }
            }

        }

        connectObservers()

    }

    private fun connectObservers() {
        viewModel.createThemeState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
        viewModel.updateThemeState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
        viewModel.createTestState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
        viewModel.updateTestState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
        viewModel.createThemeTestState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
        viewModel.payTopicState.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is NetworkState.Loading -> {
                        changeUiStateEnabled(true, progressIndicator, btnAddTest)
                    }
                    is NetworkState.Success -> {
                        changeUiStateEnabled(false, progressIndicator, btnAddTest)
                        addCLick.invoke(true)
                        dismiss()
                    }
                    is NetworkState.Error -> {
                        changeUiStateEnabled(false, progressIndicator, btnAddTest)
                        showSnackbar(resource.exception.message.toString())
                    }
                    is NetworkState.GenericError -> {
                        changeUiStateEnabled(false, progressIndicator, btnAddTest)
                        showSnackbar(resource.errorResponse.message)
                    }
                }

            }
        })
    }

    private fun stateHandler(resource: NetworkState<Any>) {
        when (resource) {
            is NetworkState.Loading -> {
                changeUiStateEnabled(true, progressIndicator, btnAddTest)
            }
            is NetworkState.Success -> {
                changeUiStateEnabled(false, progressIndicator, btnAddTest)
                addCLick.invoke(true)
                dismiss()
            }
            is NetworkState.Error -> {
                changeUiStateEnabled(false, progressIndicator, btnAddTest)
                showSnackbar(resource.exception.message.toString())
            }
            is NetworkState.GenericError -> {
                changeUiStateEnabled(false, progressIndicator, btnAddTest)
                showSnackbar(resource.errorResponse.message)
            }
        }
    }


    companion object {
        fun newInstance(
            dialogEvent: DialogEvent
        ): AddTestDialog {
            val args: Bundle = Bundle()
            args.putSerializable("event", dialogEvent)
            val fragment = AddTestDialog()
            fragment.arguments = args
            return fragment
        }
    }
}