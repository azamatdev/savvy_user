package uz.mymax.savvyenglish.ui.tests

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_test_finished.*
import uz.mymax.savvyenglish.R

class TestFinishedFragment : Fragment() {

    val args: TestFinishedFragmentArgs by navArgs()
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
        testCorrectAnswer.text = args.correctAnswer.toString() + " ta to'g'ri"


        testSolvedTime.text = args.testFinishedTime

        seeTestResultButton.setOnClickListener {
            val action = TestFinishedFragmentDirections.toQuestionsAnswers(args.questions)
            findNavController().navigate(action)
        }
        comeBackToTests.setOnClickListener {
            findNavController().popBackStack(R.id.destTests, false)
        }
    }
}