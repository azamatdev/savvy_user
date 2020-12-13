package uz.mymax.savvyenglish.ui.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_test_set.*
import kotlinx.android.synthetic.main.layout_bottom_update_delete.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.MainActivity
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.data.getOrderStartTime
import uz.mymax.savvyenglish.data.isTimeStarted
import uz.mymax.savvyenglish.data.*
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.utils.*
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration
import java.util.*
import kotlin.collections.ArrayList


class QuestionSetFragment : Fragment() {

    private lateinit var adapter: QuestionSetAdapter
    private lateinit var snapHelper: SnapHelper
    private var currentItem = 0
    private val viewModel: QuestionSetViewModel by viewModel()
    private val args: QuestionSetFragmentArgs by navArgs()
    private lateinit var bottomSheet: BottomSheetDialog
    private val sharedViewModel: SharedViewModel by inject()
    val scope = CoroutineScope(Job() + Dispatchers.Main)
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
        checkViewForAdmin(addQuestionButton)
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
//        snapHelper.attachToRecyclerView(testSetRecycler)
        testSetRecycler.addItemDecoration(LinearVerticalDecoration())

        progressSet.max = adapter.itemCount
        progressSet.setProgressCompat(0, true)

//        testSetRecycler.attachSnapHelperWithListener(
//            snapHelper,
//            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
//            object :
//                OnSnapPositionChangeListener {
//                override fun onSnapPositionChange(position: Int) {
//                    Log.d("CurrentItemTag", position.toString())
//                    currentItem = position
//                    if (position != RecyclerView.NO_POSITION)
//                        progressSet.setProgressCompat(position, true)
//                    else
//                        progressSet.setProgressCompat(progressSet.max, true)
//
//                }
//            })

        connectObservers()

        doneButton.setOnClickListener {
            var correctAnswersCount = 0;
            for (value in adapter.answersHashmap.entries) {
                if (value.value) {
                    correctAnswersCount++
                }
            }
            Log.d("CorrectAnswerTag", correctAnswersCount.toString())

            adapter.tests?.let {
                setOrderStartTime(0)
                setTimeStarted(false)
                scope.cancel()

                it.forEachIndexed { index, question ->
                    Log.d(
                        "TagCheckFirst",
                        "$index : ${question.isChecked}  ${question.checkedIdName}"
                    )
                }
                val questionAction =
                    QuestionSetFragmentDirections.toTestFinished(
                        it.toTypedArray(),
                        (requireActivity() as MainActivity).questionTime.text.toString()
                    )
                questionAction.correctAnswer = correctAnswersCount
                findNavController().navigate(questionAction)
            }

        }

        addQuestionButton.setOnClickListener {
            findNavController().navigate(QuestionSetFragmentDirections.toCreateQuestion(args.testId))
        }
        startTime()
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
                    changeUiStateVisibility(true, progressIndicator, testSetRecycler)
                }
                is NetworkState.Success -> {
                    changeUiStateVisibility(false, progressIndicator, testSetRecycler)
                    val list = resource.data as ArrayList<QuestionResponse>
//                    var newList = ArrayList<QuestionResponse>()
//                    newList.addAll(list)
//                    var newList2 = ArrayList<QuestionResponse>()
//                    newList2.addAll(list)
//                    var newList3 = ArrayList<QuestionResponse>()
//                    newList3.addAll(list)
//                    list.addAll(newList)
//                    list.addAll(newList2)
//                    list.addAll(newList3)
                    adapter.updateList(list)
                    progressSet.max = adapter.itemCount
                }
                is NetworkState.Error -> {
                    changeUiStateVisibility(false, progressIndicator, testSetRecycler)
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    changeUiStateVisibility(false, progressIndicator, testSetRecycler)
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

    private fun startCounting() {
        scope.launch {
            if (isTimeStarted()) {
                val starTime = getOrderStartTime()
                //TODO take time from server
                if (starTime != 0L) {
                    val difference = differenceBetweenNowAndStartTime(starTime)
                    val seconds = difference / 1000
                    for (second in seconds until 36000) {
                        val minute = second / 60
                        val seconds = second % 60
                        delay(1000)
                        Log.d("CounterTag", String.format("%d:%02d", minute, seconds))
                        sharedViewModel.setTime(String.format("%d:%02d", minute, seconds))
                    }
                }
            }

        }

        sharedViewModel.timerLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                (requireActivity() as MainActivity).questionTime.text = it
            }
        })
    }

    fun startTime() {
        if (!isTimeStarted()) {
            val calendar = Calendar.getInstance()
            setOrderStartTime(calendar.timeInMillis)
            setTimeStarted(true)
            startCounting()
        }
    }

    override fun onDestroy() {
        setOrderStartTime(0)
        setTimeStarted(false)
        scope.cancel()
        super.onDestroy()
    }

    override fun onResume() {
        startCounting()
        super.onResume()
    }

    private fun differenceBetweenNowAndStartTime(startTime: Long): Long {
        val calendar = Calendar.getInstance()
        if (startTime != 0L) {
            return calendar.timeInMillis - startTime
        }
        return 0L
    }

}