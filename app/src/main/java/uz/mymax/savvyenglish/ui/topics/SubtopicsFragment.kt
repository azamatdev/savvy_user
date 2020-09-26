package uz.mymax.savvyenglish.ui.topics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_subtopic.*
import kotlinx.android.synthetic.main.fragment_topics.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.ui.base.BaseFragment
import uz.mymax.savvyenglish.ui.topics.adapter.SubtopicAdapter
import uz.mymax.savvyenglish.utils.PlaceHolderAdapter
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration
import uz.mymax.savvyenglish.utils.hideLoading
import uz.mymax.savvyenglish.utils.showLoading
import uz.mymax.savvyenglish.utils.showSnackbar


class SubtopicsFragment : BaseFragment() {


    private val viewModel: LessonViewModel by viewModel()

    private var storedView: View? = null

    private lateinit var adapter: SubtopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchSubtopics()
        adapter = SubtopicAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subtopic, container, false)
        return if (storedView != null) {
            storedView
        } else {
            storedView = view
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subtopicRecycler.adapter =
            PlaceHolderAdapter(R.layout.item_topic_placeholder)

        subtopicRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        subtopicRecycler.addItemDecoration(LinearVerticalDecoration())

        viewModel.topicsResource.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    swipeToRefreshLayout.showLoading()
                }
                is Resource.Success -> {
                    swipeToRefreshLayout.hideLoading()
//                    adapter.updateList(resource.data)
                    topicsRecycler.adapter = adapter
                    topicsRecycler.scheduleLayoutAnimation()
                }
                is Resource.Error -> {
                    swipeToRefreshLayout.hideLoading()
                    showSnackbar(resource.exception.message.toString())
                }
                is Resource.GenericError -> {
                    swipeToRefreshLayout.hideLoading()
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
    }
}
