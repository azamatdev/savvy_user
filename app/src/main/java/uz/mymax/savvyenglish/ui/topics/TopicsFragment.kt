package uz.mymax.savvyenglish.ui.topics

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_topics.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.ui.topics.adapter.TopicsAdapter
import uz.mymax.savvyenglish.utils.PlaceHolderAdapter
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration
import uz.mymax.savvyenglish.utils.showSnackbar
import uz.mymax.savvyenglish.utils.hideLoading
import uz.mymax.savvyenglish.utils.showLoading


class TopicsFragment : Fragment() {

    private val viewModel: LessonViewModel by viewModel()
    private lateinit var adapter: TopicsAdapter
    private var storedView: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (storedView == null) {
            storedView = inflater.inflate(R.layout.fragment_topics, container, false)
            storedView
        } else
            storedView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchTopics()
        adapter = TopicsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topicsRecycler.adapter = PlaceHolderAdapter(R.layout.item_topic_placeholder)
        adapter.itemClickListener = {
            findNavController().navigate(R.id.destSubtopic)
        }

        topicsRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        topicsRecycler.addItemDecoration(LinearVerticalDecoration())
        viewModel.topicsResource.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    topicsSwipeRefreshLayout.showLoading()
                }
                is Resource.Success -> {
                    topicsSwipeRefreshLayout.hideLoading()
                    adapter.updateList(resource.data)
                    topicsRecycler.adapter = adapter
                    topicsRecycler.scheduleLayoutAnimation()
                }
                is Resource.Error -> {
                    topicsSwipeRefreshLayout.hideLoading()
                    showSnackbar(resource.exception.message.toString())
                }
                is Resource.GenericError -> {
                    topicsSwipeRefreshLayout.hideLoading()
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
        topicsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchTopics()
        }
    }


}
