package uz.mymax.savvyenglish.ui.lessons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.data.LessonData
import uz.mymax.savvyenglish.data.LessonQuestionTypeForm
import uz.mymax.savvyenglish.data.LessonRule
import uz.mymax.savvyenglish.data.LessonType
import uz.mymax.savvyenglish.databinding.ItemLessonQuestionBinding
import uz.mymax.savvyenglish.databinding.ItemLessonRuleBinding

class LessonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = LessonData.getLessonList()

    override fun getItemViewType(position: Int): Int {
        return when (list[position].getType()) {
            LessonType.RULE -> 0
            LessonType.QUESTION -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view =
                ItemLessonRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LessonVH(view.root)
        } else {
            val view = ItemLessonQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            QuestionVH(view.root)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LessonVH -> {
                val rule = (list[position] as LessonRule)
                holder.binding!!.rule = rule
            }
            is QuestionVH -> {
                val question = (list[position] as LessonQuestionTypeForm)
                holder.binding!!.questionTypeForm = question
            }
        }
    }

    class LessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.getBinding<ItemLessonRuleBinding>(itemView)
    }

    class QuestionVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = DataBindingUtil.getBinding<ItemLessonQuestionBinding>(itemView)
    }
}