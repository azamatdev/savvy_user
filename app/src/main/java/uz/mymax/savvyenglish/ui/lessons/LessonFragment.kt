package uz.mymax.savvyenglish.ui.lessons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_lesson.*
import uz.mymax.savvyenglish.R


/**
 * A simple [Fragment] subclass.
 */
class LessonFragment : Fragment(R.layout.fragment_lesson) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lessonRecycler.adapter = LessonAdapter()

    }
}
