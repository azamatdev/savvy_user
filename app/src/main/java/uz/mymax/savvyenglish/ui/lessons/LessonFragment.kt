package uz.mymax.savvyenglish.ui.lessons

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_lesson.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.Resource
import uz.mymax.savvyenglish.ui.base.BaseFragment


class LessonFragment : BaseFragment() {


    private val viewModel: LessonViewModel by viewModel()

    private var storedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lesson, container, false)
        return if (storedView != null) {
            storedView
        } else {
            storedView = view
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lessonRecycler.adapter = LessonAdapter()

        viewModel.topicsResource.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> Log.d("LiveDataTag", "Loading")
                is Resource.Success -> Log.d("LiveDataTag", "Success:")
                is Resource.Error -> Log.d("LiveDataTag", "Error: " + it.exception.message)
            }
        })
        viewModel.fetchTopics()
    }
}
