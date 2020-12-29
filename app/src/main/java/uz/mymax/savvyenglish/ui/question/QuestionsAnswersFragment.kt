package uz.mymax.savvyenglish.ui.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_questions_answers.*
import kotlinx.android.synthetic.main.fragment_questions_answers.view.*
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.network.response.MockData
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.utils.decorations.LinearVerticalDecoration


class QuestionsAnswersFragment : Fragment() {
    private lateinit var adapter: QuestionCheckAdapter
    private val args: QuestionsAnswersFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentView = inflater.inflate(R.layout.fragment_questions_answers, container, false)
        currentView.testCheckRecycler.addItemDecoration(LinearVerticalDecoration())
        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = QuestionCheckAdapter()
        val list = args.questions.toMutableList() as ArrayList<QuestionResponse>
        adapter.updateList(list)
        testCheckRecycler.adapter = adapter

        comeBackToTests.setOnClickListener {
            findNavController().popBackStack(R.id.destTests, false)
        }
    }
}