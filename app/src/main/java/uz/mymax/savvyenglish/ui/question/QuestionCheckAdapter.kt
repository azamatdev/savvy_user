package uz.mymax.savvyenglish.ui.question

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.databinding.ItemTestCheckBinding
import uz.mymax.savvyenglish.databinding.ItemTestSetBinding
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.utils.isAdmin

class QuestionCheckAdapter :
    RecyclerView.Adapter<QuestionCheckAdapter.TestVH>() {
    var tests: ArrayList<QuestionResponse>? = null
    fun updateList(tests: ArrayList<QuestionResponse>) {
        this.tests = tests
        tests.forEachIndexed{index, question ->
            Log.d("TagCheck", "$index : ${question.isChecked}  ${question.checkedIdName}")
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemTestCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestVH(view.root)
    }

    override fun getItemCount() = tests?.size ?: 0

    override fun onBindViewHolder(holder: TestVH, position: Int) {
//        holder.binding?.testTitle?.text = tests!![position]
        holder.setIsRecyclable(false)
        val test = tests!![holder.adapterPosition]
        holder.binding?.question = test
        holder.binding?.testQuestion?.text =
            (position + 1).toString() + "." + tests!![holder.adapterPosition].text
        val isCorrect = test.checkedIdName == test.correctAns


        holder.binding?.correctAnswer?.text = "Correct Answer: " + test.correctAns
        if (isCorrect) {
            holder.binding?.correctAnswer?.setBackgroundColor(Color.parseColor("#22b26d"))
            holder.binding?.correctAnswer?.strokeColor =
                holder.itemView.context.resources.getColorStateList(R.color.button_stroke_color_correct)
        } else {
            holder.binding?.correctAnswer?.setBackgroundColor(Color.parseColor("#ff735c"))
            holder.binding?.correctAnswer?.strokeColor =
                holder.itemView.context.resources.getColorStateList(R.color.button_stroke_color_wrong)
        }

//        Log.d("TagCheck", "$position : ${test.isChecked}  ${test.checkedIdName}")
        if (tests!![position].isChecked)
            when (tests!![holder.adapterPosition].checkedIdName) {
                "A" -> {
                    holder.binding?.questionToggleGroup?.check(R.id.A)
                }
                "B" -> {
                    holder.binding?.questionToggleGroup?.check(R.id.B)
                }
                "C" -> {
                    holder.binding?.questionToggleGroup?.check(R.id.C)
                }
                "D" -> {
                    holder.binding?.questionToggleGroup?.check(R.id.D)
                }
            }
        else {
            holder.binding?.questionToggleGroup?.clearChecked()
        }
    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemTestCheckBinding? = DataBindingUtil.bind<ItemTestCheckBinding>(itemView)
    }

    companion object {
        private val SHOPS_COMPARATOR = object : DiffUtil.ItemCallback<QuestionResponse>() {
            override fun areItemsTheSame(
                oldItem: QuestionResponse,
                newItem: QuestionResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: QuestionResponse,
                newItem: QuestionResponse
            ): Boolean = oldItem == newItem
        }
    }
}