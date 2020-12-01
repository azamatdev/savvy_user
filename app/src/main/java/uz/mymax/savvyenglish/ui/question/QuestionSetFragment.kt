package uz.mymax.savvyenglish.ui.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_test_set.*
import kotlinx.android.synthetic.main.layout_bottom_update_delete.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.ui.tests.TestEvent
import uz.mymax.savvyenglish.ui.tests.admin.AddTestDialog
import uz.mymax.savvyenglish.ui.tests.admin.DialogEvent
import uz.mymax.savvyenglish.utils.*
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration


class QuestionSetFragment : Fragment() {

    private lateinit var adapter: QuestionSetAdapter
    private lateinit var snapHelper: SnapHelper
    private var currentItem = 0
    private val viewModel: QuestionSetViewModel by viewModel()
    private val args: QuestionSetFragmentArgs by navArgs()
    private lateinit var bottomSheet: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = QuestionSetAdapter()
        snapHelper = SnapHelperOneByOne()
        viewModel.setStateQuestion(QuestionEvent.GetQuestionsOfTest(args.testId))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testSetRecycler.adapter = adapter
        adapter.onLongClickListener = { question, position ->
            bottomSheet = createBottomSheet(R.layout.layout_bottom_update_delete)
            bottomSheet.show()

            bottomSheet.deleteButton.setOnClickListener {
                viewModel.setStateQuestion(
                    QuestionEvent.DeleteQuestion(
                        question.id.toString(),
                        position
                    )
                )
                bottomSheet.dismiss()
            }

            bottomSheet.updateButton.setOnClickListener {
                val action = QuestionSetFragmentDirections.toCreateQuestion(args.testId)
                action.isUpdating = true
                action.question = question
                findNavController().navigate(action)
                bottomSheet.dismiss()
            }
        }
        snapHelper.attachToRecyclerView(testSetRecycler)
        testSetRecycler.addItemDecoration(LinearVerticalDecoration())

        progressSet.max = adapter.itemCount
        progressSet.setProgressCompat(0, true)

        testSetRecycler.attachSnapHelperWithListener(
            snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            object :
                OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    Log.d("CurrentItemTag", position.toString())
                    currentItem = position
                    if (position != RecyclerView.NO_POSITION)
                        progressSet.setProgressCompat(position, true)
                    else
                        progressSet.setProgressCompat(progressSet.max, true)

                }
            })

        connectObservers()

        doneButton.setOnClickListener {
            var correctAnswersCount = 0;
            for (value in adapter.answersHashmap.entries) {
                if (value.value) {
                    correctAnswersCount++
                }
            }
            Log.d("CorrectAnswerTag", correctAnswersCount.toString())
            findNavController().navigate(
                R.id.toTestFinished,
                bundleOf("correctAnswer" to correctAnswersCount)
            )
        }

        addQuestionButton.setOnClickListener {
            findNavController().navigate(QuestionSetFragmentDirections.toCreateQuestion(args.testId))
        }


    }

    private fun connectObservers() {
        viewModel.allQuestionsState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    adapter.updateList(resource.data as ArrayList<QuestionResponse>)
                    progressSet.max = adapter.itemCount
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
        viewModel.questionsOfTestState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    adapter.updateList(resource.data as ArrayList<QuestionResponse>)
                    progressSet.max = adapter.itemCount
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
        viewModel.deleteQuestionState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    adapter.removedCart(resource.data.toInt())
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
    }

    fun observeStates(resource: NetworkState<Any>) {

    }
}