package uz.mymax.savvyenglish.ui.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_subtopic.*
import kotlinx.android.synthetic.main.fragment_subtopic.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.response.SubtopicResponse
import uz.mymax.savvyenglish.ui.topics.adapter.SubtopicsAdapter
import uz.mymax.savvyenglish.utils.PlaceHolderAdapter
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration
import uz.mymax.savvyenglish.utils.hideLoading
import uz.mymax.savvyenglish.utils.showLoading
import uz.mymax.savvyenglish.utils.showSnackbar


class SubtopicsFragment : Fragment() {


    private val viewModel: TopicViewModel by viewModel()

    private var storedView: View? = null

    private lateinit var adapter: SubtopicsAdapter

    private val args: SubtopicsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchSubtopics(args.topicId.toString())
        adapter = SubtopicsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (storedView == null) {
            storedView = inflater.inflate(R.layout.fragment_subtopic, container, false)
            storedView!!.subtopicRecycler.layoutAnimation =
                AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    R.anim.layout_animation_fall_down
                )
            storedView!!.subtopicRecycler.addItemDecoration(LinearVerticalDecoration())
            storedView!!.subtopicRecycler.adapter =
                PlaceHolderAdapter(R.layout.item_topic_placeholder)
            return storedView
        } else
            return storedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.itemClickListener = { id, title ->
            val action = SubtopicsFragmentDirections.toExplanation()
            action.subtopicId = id
            action.subtopicTitle = title
            findNavController().navigate(action)
        }
        viewModel.subtopicResource.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    swipeToRefreshLayout.showLoading()
                }
                is Resource.Success -> {
                    swipeToRefreshLayout.hideLoading()
                    val list = ArrayList(resource.data)
                    list.addAll(list)
                    list.addAll(list)
                    list.addAll(list)
                    list.addAll(list)
                    list.addAll(list)
                    list.addAll(list)
                    adapter.updateList(list)
                    subtopicRecycler.adapter = adapter
                    subtopicRecycler.scheduleLayoutAnimation()
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

        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.fetchSubtopics(args.topicId.toString())
        }
    }

}
