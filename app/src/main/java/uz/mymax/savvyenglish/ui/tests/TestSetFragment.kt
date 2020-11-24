package uz.mymax.savvyenglish.ui.tests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.fragment_test_set.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.network.response.QuestionItem
import uz.mymax.savvyenglish.ui.tests.adapter.TestSetAdapter
import uz.mymax.savvyenglish.utils.*
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration


class TestSetFragment : Fragment() {

    private lateinit var adapter: TestSetAdapter
    private lateinit var snapHelper: SnapHelper
    private var currentItem = 0
    private val viewModel: TestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TestSetAdapter()
        snapHelper = SnapHelperOneByOne()
        viewModel.fetchTestSet()
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


        viewModel.testSetState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    adapter.updateList(resource.data as ArrayList<QuestionItem>)
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

    }
}