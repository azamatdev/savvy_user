package uz.mymax.savvyenglish.ui.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_create_question.*
import kotlinx.android.synthetic.main.fragment_create_question.addQuestionButton
import kotlinx.android.synthetic.main.fragment_test_set.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.utils.*


class CreateQuestionFragment : Fragment() {

    private val viewModel: QuestionSetViewModel by viewModel()
    private lateinit var inputArrayList: ArrayList<TextInputEditText>
    private lateinit var layoutArrayList: ArrayList<TextInputLayout>
    private val args: CreateQuestionFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInputUi()

        updateUi()

        addQuestionButton.setOnClickListener {

            if (inputArrayList.areAllFieldsFilled()) {
                if (getCheckedAnswer() != "")
                    viewModel.setStateQuestion(
                        QuestionEvent.CreateQuestion(
                            getQuestion(),
                            args.testId
                        )
                    )
                else
                    showSnackbar("Check at least one answer")
            } else {
                inputArrayList.showErrorIfNotFilled(layoutArrayList)
            }
        }
        viewModel.createQuestionState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                    changeUiStateEnabled(true, progressIndicator, addQuestionButton)
                }
                is NetworkState.Success -> {
                    hideKeyboard()
                    changeUiStateEnabled(false, progressIndicator, addQuestionButton)
                    val action = CreateQuestionFragmentDirections.toQuestionSet()
                    action.testId = args.testId
                    findNavController().navigate(action)
                }
                is NetworkState.Error -> {
                    changeUiStateEnabled(false, progressIndicator, addQuestionButton)
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    changeUiStateEnabled(false, progressIndicator, addQuestionButton)
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
    }

    private fun updateUi() {
        if (args.isUpdating) {
            questionTitleInput.setText(args.question?.title)
            questionTextInput.setText(args.question?.text)
            answerAInput.setText(args.question?.ansA)
            answerBInput.setText(args.question?.ansB)
            answerCInput.setText(args.question?.ansC)
            answerDInput.setText(args.question?.ansD)
            when (args.question?.correctAns) {
                "A" -> {
                    ansA.isPressed = true
                }
                "B" -> {
                    ansB.isPressed = true
                }
                "C" -> {
                    ansC.isPressed = true
                }
                "D" -> {
                    ansD.isPressed = true
                }
            }
        }
    }

    private fun getQuestion(): QuestionResponse {
        val question = QuestionResponse()
        question.title = questionTitleInput.getStringText()
        question.text = questionTextInput.getStringText()
        question.ansA = answerAInput.getStringText()
        question.ansB = answerBInput.getStringText()
        question.ansC = answerCInput.getStringText()
        question.ansD = answerDInput.getStringText()

        question.correctAns = getCheckedAnswer()

        question.base = "String"
        Log.d("TagCheck", question.toString())

        return question
    }


    private fun getCheckedAnswer(): String {
        return when {
            ansA.isFocused -> "A"
            ansB.isFocused -> "B"
            ansC.isFocused -> "C"
            ansD.isFocused -> "D"
            else -> ""
        }
    }

    private fun initInputUi() {
        inputArrayList = ArrayList<TextInputEditText>()
        inputArrayList.add(questionTitleInput)
        inputArrayList.add(questionTextInput)
        inputArrayList.add(answerAInput)
        inputArrayList.add(answerBInput)
        inputArrayList.add(answerCInput)
        inputArrayList.add(answerDInput)
        layoutArrayList = ArrayList<TextInputLayout>()
        layoutArrayList.add(questionTitleLayout)
        layoutArrayList.add(questionTextLayout)
        layoutArrayList.add(answerALayout)
        layoutArrayList.add(answerBLayout)
        layoutArrayList.add(answerCLayout)
        layoutArrayList.add(answerDLayout)

        inputArrayList.hideErrorIfFilled(layoutArrayList)


    }
}