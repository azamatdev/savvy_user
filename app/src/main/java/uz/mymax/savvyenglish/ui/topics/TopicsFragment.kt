package uz.mymax.savvyenglish.ui.topics

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.android.synthetic.main.fragment_topics.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.data.isAdmin
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.ui.topics.adapter.TopicsAdapter
import uz.mymax.savvyenglish.utils.PlaceHolderAdapter
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration
import uz.mymax.savvyenglish.utils.showSnackbar
import uz.mymax.savvyenglish.utils.hideLoading
import uz.mymax.savvyenglish.utils.showLoading


class TopicsFragment : Fragment() {

    private val viewModel: TopicViewModel by viewModel()
    private lateinit var adapter: TopicsAdapter
    private var storedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchTopics()
        adapter = TopicsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (storedView == null) {
            storedView = inflater.inflate(R.layout.fragment_topics, container, false)
            storedView?.addTopicButton?.isVisible = requireContext().isAdmin()
            storedView!!.topicsRecycler.layoutAnimation =
                AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    R.anim.layout_animation_fall_down
                )
            storedView!!.topicsRecycler.addItemDecoration(LinearVerticalDecoration())
            storedView!!.topicsRecycler.adapter =
                PlaceHolderAdapter(R.layout.item_topic_placeholder)
            return storedView
        } else
            return storedView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.itemClickListener = {
            val action = TopicsFragmentDirections.actionDestTopicsToDestSubtopic()
            action.topicId = it
            findNavController().navigate(action)
        }
        viewModel.topicsResource.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                    topicsSwipeRefreshLayout.showLoading()
                }
                is NetworkState.Success -> {
                    topicsSwipeRefreshLayout.hideLoading()
                    adapter.updateList(resource.data)
                    topicsRecycler.adapter = adapter
                    topicsRecycler.scheduleLayoutAnimation()
                }
                is NetworkState.Error -> {
                    topicsSwipeRefreshLayout.hideLoading()
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    topicsSwipeRefreshLayout.hideLoading()
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
        topicsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchTopics()
        }

        addTopicButton.setOnClickListener {
        }
    }


}
