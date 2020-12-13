package uz.mymax.savvyenglish.ui.question

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
import uz.mymax.savvyenglish.databinding.ItemTestSetBinding
import uz.mymax.savvyenglish.network.response.QuestionResponse
import uz.mymax.savvyenglish.utils.isAdmin

class QuestionSetAdapter :
    RecyclerView.Adapter<QuestionSetAdapter.TestVH>() {

    var onLongClickListener: ((QuestionResponse, Int) -> Unit)? = null
    var itemClickListener: ((Int) -> Unit)? = null
    var tests: ArrayList<QuestionResponse>? = null
    var answersHashmap = HashMap<String, Boolean>()
    var sparseBoolean = SparseBooleanArray()


    fun updateList(tests: ArrayList<QuestionResponse>) {
        this.tests = tests
        notifyDataSetChanged()
    }

    fun removedCart(updatedPosition: Int) {
        tests!!.removeAt(updatedPosition)
        notifyItemRemoved(updatedPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemTestSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        holder.itemView.setOnLongClickListener {
            if (holder.itemView.context.isAdmin()) {
                onLongClickListener?.invoke(tests!![holder.adapterPosition], holder.adapterPosition)
            }
            return@setOnLongClickListener true
        }

        if (tests!![position].isChecked) {
            when (tests!![holder.adapterPosition].checkedIdName) {
                "A" -> holder.binding?.questionToggleGroup?.check(R.id.A)
                "B" -> holder.binding?.questionToggleGroup?.check(R.id.B)
                "C" -> holder.binding?.questionToggleGroup?.check(R.id.C)
                "D" -> holder.binding?.questionToggleGroup?.check(R.id.D)
            }
        } else {
            holder.binding?.A?.isChecked = false
            holder.binding?.B?.isChecked = false
            holder.binding?.C?.isChecked = false
            holder.binding?.D?.isChecked = false
        }

        holder.bindViews()


    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemTestSetBinding? = DataBindingUtil.bind<ItemTestSetBinding>(itemView)
        fun bindViews() {
            binding?.questionToggleGroup?.addOnButtonCheckedListener { group, checkedId, isChecked ->
                check(checkedId, isChecked)
            }
        }

        private fun check(checkedId: Int, isChecked: Boolean) {
            if (isChecked) {
                answersHashmap.put(
                    "" + position + tests!![adapterPosition].id,
                    tests!![adapterPosition].correctAns == itemView.context.resources.getResourceEntryName(
                        checkedId
                    )
                )

                sparseBoolean.put(adapterPosition, true)

                tests!![adapterPosition].checkedIdName =
                    itemView.context.resources.getResourceEntryName(
                        checkedId
                    )
                tests!![position].isChecked = true
                Log.d("TagCheck3", "Times :" + position)
                Log.d("TagCheck3", "Times :" + tests!![position].isChecked)
                tests!!.forEachIndexed { index, question ->
                    Log.d(
                        "TagCheck2",
                        "$index : ${question.isChecked}  ${question.checkedIdName}"
                    )
                }

            }
        }
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