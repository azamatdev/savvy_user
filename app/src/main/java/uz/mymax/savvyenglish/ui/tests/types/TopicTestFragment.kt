package uz.mymax.savvyenglish.ui.tests.types

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_topic_related.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.ui.tests.TestViewModel
import uz.mymax.savvyenglish.ui.tests.adapter.TopicTestAdapter
import uz.mymax.savvyenglish.utils.VerticalSpaceItemDecoration
import uz.mymax.savvyenglish.utils.showSnackbar

class TopicTestFragment : Fragment() {

    private val viewModel: TestViewModel by viewModel()
    private lateinit var adapter: TopicTestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TopicTestAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic_related, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topicTestRecycler.adapter = adapter
        topicTestRecycler.addItemDecoration(VerticalSpaceItemDecoration(16))
        viewModel.topicTestState.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                }
                is NetworkState.Success -> {
                    adapter.updateList(resource.data)
                }
                is NetworkState.Error -> {
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })

        adapter.itemClickListener = {
            findNavController().navigate(
                R.id.destTestSet,
                bundleOf("fromTopic" to true, "setId" to it)
            )
        }

    }

    override fun onResume() {
        if (adapter.itemCount == 0)
            viewModel.fetchTopicTests()
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TopicTestFragment()
    }
}