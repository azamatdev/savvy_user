package uz.mymax.savvyenglish.ui.topics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_topics.*
import kotlinx.coroutines.*

import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.ui.topics.adapter.StickyHeaderInterface
import uz.mymax.savvyenglish.ui.topics.adapter.StickyHeaderItemDecoration
import uz.mymax.savvyenglish.ui.topics.adapter.TopicsAdapter
import uz.mymax.savvyenglish.ui.topics.adapter.TopicsPlaceholderAdapter
import uz.mymax.savvyenglish.utils.initToolbar


class TopicsFragment : Fragment(R.layout.fragment_topics) {

//    private val job = Job()
    private val scopeMainThread = CoroutineScope( Dispatchers.Default)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(topicsToolbar)
        val adapter = TopicsAdapter {position -> onAdapterCallback(position)}
        topicsRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context!!, R.anim.layout_animation_fall_down)
        topicsRecycler.adapter = TopicsPlaceholderAdapter()

        scopeMainThread.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                topicsRecycler.scheduleLayoutAnimation()
                topicsRecycler.adapter = adapter
            }
        }
    }

    private fun onAdapterCallback(position : Int){
        Log.d("TagCheck", "Clicked")
        findNavController().navigate(R.id.action_topicsFragment_to_lessonFragment)
    }

    override fun onStop() {
//        job.cancel()
        super.onStop()
    }

    override fun onStart() {
//        job?.start()
        super.onStart()
    }
}
