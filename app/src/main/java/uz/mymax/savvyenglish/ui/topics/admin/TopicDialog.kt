package uz.mymax.savvyenglish.ui.topics.admin

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
import kotlinx.android.synthetic.main.dialog_topic.*
import org.koin.android.ext.android.inject
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.dto.CreateTestDto
import uz.mymax.savvyenglish.network.dto.ThemeUpdateDto
import uz.mymax.savvyenglish.network.response.SubtopicResponse
import uz.mymax.savvyenglish.network.response.ThemeTestResponse
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.ui.tests.TestEvent
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.tests.ThemeEvent
import uz.mymax.savvyenglish.ui.topics.TopicEvent
import uz.mymax.savvyenglish.ui.topics.TopicViewModel
import uz.mymax.savvyenglish.utils.*
import java.io.Serializable


sealed class TopicDialogEvent : Serializable {
    object CreateTopic : TopicDialogEvent()
    data class UpdateTopic(val topic: TopicResponse) : TopicDialogEvent()
    data class CreateSubtopic(val topicId: String) : TopicDialogEvent()
    data class UpdateSubtopic(val subtopic: SubtopicResponse) :
        TopicDialogEvent()

}

class TopicDialog : DialogFragment() {

    lateinit var addCLick: ((Boolean) -> Unit)
    private val viewModel: TopicViewModel by inject()
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
            R.layout.dialog_topic,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val event = it.getSerializable("event") as TopicDialogEvent
            when (event) {
                is TopicDialogEvent.UpdateTopic -> {
                    topicTitleInput.setText(event.topic.title)
                    btnAddTest.text = "Update"
                }
                is TopicDialogEvent.UpdateSubtopic -> {
                    topicTitleInput.setText(event.subtopic.title)
                    btnAddTest.text = "Update"
                }
                else -> null
            }
        }

        topicTitleInput.hideErrorIfFilled()
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
            if (topicTitleInput.text.toString().isEmpty()) {
                topicTitleInput.showErrorIfNotFilled()
                return@setOnClickListener
            }
            if (paidCheckbox.isChecked) {
                if (priceTestInput.text.toString().isEmpty()) {
                    priceTestInput.showErrorIfNotFilled()
                    return@setOnClickListener
                }
            }
            val event = arguments?.getSerializable("event") as TopicDialogEvent

            when (event) {
                is TopicDialogEvent.CreateTopic -> {
                    val newTopic = TopicResponse(
                        title = topicTitleInput.text.toString()
                    )
                    viewModel.setTopicState(TopicEvent.CreateTopic(newTopic))
                }
                is TopicDialogEvent.UpdateTopic -> {
                    val updatedTopic = TopicResponse(
                        title = topicTitleInput.text.toString(),
                        id = event.topic.id
                    )
                    viewModel.setTopicState(
                        TopicEvent.UpdateTopic(
                            updatedTopic,
                            position = updatedTopic.positionUpdated
                        )
                    )
                }
                is TopicDialogEvent.CreateSubtopic -> {
                    val updatedSubTopic = SubtopicResponse(
                        title = topicTitleInput.text.toString(),
                        parentId = event.topicId.toInt()
                    )
//                    viewModel.setTopicState(TopicEvent.UpdateTopic(updatedSubTopic))
                }
                is TopicDialogEvent.UpdateSubtopic -> {
                    val updatedSubTopic = SubtopicResponse(
                        title = topicTitleInput.text.toString(),
                        parentId = event.subtopic.parentId,
                        description = event.subtopic.description,
                        id = event.subtopic.id
                    )
//                    viewModel.setTopicState(TopicEvent.UpdateTopic(updatedSubTopic))
                }

            }

        }

        connectObservers()

    }

    private fun connectObservers() {
        viewModel.createTopicsState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
        viewModel.updateTopicsState.observe(this, Observer { resource ->
            stateHandler(resource)
        })
//        viewModel.createTestState.observe(this, Observer { resource ->
//            stateHandler(resource)
//        })
//        viewModel.updateTestState.observe(this, Observer { resource ->
//            stateHandler(resource)
//        })
//        viewModel.createThemeTestState.observe(this, Observer { resource ->
//            stateHandler(resource)
//        })
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
            dialogEvent: TopicDialogEvent
        ): TopicDialog {
            val args: Bundle = Bundle()
            args.putSerializable("event", dialogEvent)
            val fragment = TopicDialog()
            fragment.arguments = args
            return fragment
        }
    }
}