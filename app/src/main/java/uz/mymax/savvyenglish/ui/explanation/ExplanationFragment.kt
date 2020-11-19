package uz.mymax.savvyenglish.ui.explanation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_explanation.*
import kotlinx.android.synthetic.main.fragment_explanation.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.NetworkState
import uz.mymax.savvyenglish.ui.topics.TopicViewModel
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration
import uz.mymax.savvyenglish.utils.hideVisibility
import uz.mymax.savvyenglish.utils.makeVisible
import uz.mymax.savvyenglish.utils.setToolbarTitle
import uz.mymax.savvyenglish.utils.showSnackbar


class ExplanationFragment : Fragment() {

    private val viewModel: TopicViewModel by viewModel()

    private var storedView: View? = null

    private lateinit var adapter: ExplanationAdapter

    private val args: ExplanationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ExplanationAdapter()
        viewModel.fetchExplanations(args.subtopicId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (storedView == null) {
            storedView = inflater.inflate(R.layout.fragment_explanation, container, false)
            storedView!!.explanationRecycler.addItemDecoration(LinearVerticalDecoration())
            storedView!!.explanationRecycler.adapter =
                adapter
            return storedView
        } else
            return storedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle(args.subtopicTitle)
        viewModel.explanationResource.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is NetworkState.Loading -> {
                    progressIndicator.makeVisible()
                }
                is NetworkState.Success -> {
                    progressIndicator.hideVisibility()
                    adapter.updateList(resource.data)

                }
                is NetworkState.Error -> {
                    progressIndicator.hideVisibility()
                    showSnackbar(resource.exception.message.toString())
                }
                is NetworkState.GenericError -> {
                    progressIndicator.hideVisibility()
                    showSnackbar(resource.errorResponse.message)
                }
            }
        })
        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.fetchSubtopics(args.subtopicId.toString())
        }


    }
}