package uz.mymax.savvyenglish.ui.tests

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_test_finished.*
import uz.mymax.savvyenglish.R

class TestFinishedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationAward.setMaxFrame(90)
        testCorrectAnswer.text = arguments?.getInt("correctAnswer").toString() + " ta to'g'ri"
//        animationAward.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationRepeat(p0: Animator?) {
//                animationAward.setMinAndMaxFrame(70, 99)
//            }
//
//            override fun onAnimationEnd(p0: Animator?) {
//            }
//
//            override fun onAnimationCancel(p0: Animator?) {
//            }
//
//            override fun onAnimationStart(p0: Animator?) {
//            }
//        })

        comeBackToTests.setOnClickListener {
            findNavController().popBackStack(R.id.destTests, false)
        }
    }
}