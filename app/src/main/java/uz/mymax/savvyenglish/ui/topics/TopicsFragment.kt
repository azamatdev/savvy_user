package uz.mymax.savvyenglish.ui.topics

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_topics.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.model.Topic
import uz.mymax.savvyenglish.model.TopicHeader
import uz.mymax.savvyenglish.model.TopicType
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.ui.base.BaseFragment
import uz.mymax.savvyenglish.ui.lessons.LessonViewModel
import uz.mymax.savvyenglish.ui.topics.adapter.TopicsAdapter
import uz.mymax.savvyenglish.ui.topics.adapter.TopicsPlaceholderAdapter
import uz.mymax.savvyenglish.utils.makeVisible


class TopicsFragment : BaseFragment() {

    private val viewModel: LessonViewModel by viewModel()
    private lateinit var adapter: TopicsAdapter
    private var storedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if(storedView == null) {
            storedView = inflater.inflate(R.layout.fragment_topics, container, false)
            initRecycler(storedView)
            storedView
        } else
            storedView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        topicsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchTopics()
        }

        viewModel.topicsResource.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    val newList: List<TopicResponse> = resource.data
                    val sortedList = ArrayList<TopicType>()

                    for (i in newList.indices) {
                        sortedList.add(TopicHeader(newList[i].partTitle))
                        for (topic in newList[i].topicList) {
                            sortedList.add(Topic(topicTitle = topic.title))
                        }
                    }
                    topicsRecycler.scheduleLayoutAnimation()
                    topicsRecycler.adapter = adapter
                    adapter.updateList(sortedList)
                    hideSwipeRefreshLoading()
                }
                is Resource.Loading -> showSwipeRefreshLoading()
                is Resource.Error -> {
                    hideSwipeRefreshLoading()
                    handleErrors(resource.exception)
                }
            }
        })
        if(adapter.itemCount == 0)
        viewModel.fetchTopics()
    }

    private fun initRecycler(view: View?) {
        adapter = TopicsAdapter { position -> onAdapterCallback(position) }
        view?.findViewById<RecyclerView>(R.id.topicsRecycler)?.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        view?.findViewById<RecyclerView>(R.id.topicsRecycler)?.adapter = TopicsPlaceholderAdapter()

    }

    private fun showSwipeRefreshLoading() {
        topicsSwipeRefreshLayout.makeVisible()
        topicsSwipeRefreshLayout.isRefreshing = true
    }

    private fun hideSwipeRefreshLoading() {
        topicsSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_topics, menu)

        context?.let { context ->
            //            val searchView = menu.findItem(R.id.search).actionView as? SearchView
//            val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
//            val componentName = ComponentName(context, MainActivity::class.java)
//
//            searchManager!!.setOnCancelListener {
//                toolbar.setNavigationIcon(R.drawable.ic_back)
//            }
//            searchView?.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun onAdapterCallback(position: Int) {
        Log.d("TagCheck", "Clicked")
        findNavController().navigate(R.id.action_navigation_topics_to_navigation_lesson)
    }

}
