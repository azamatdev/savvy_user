package uz.mymax.savvyenglish.ui.tests.adapter

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_test_set.view.*
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.databinding.ItemTestSetBinding
import uz.mymax.savvyenglish.network.response.QuestionItem

class TestSetAdapter : RecyclerView.Adapter<TestSetAdapter.TestVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var tests: ArrayList<QuestionItem>? = null
    var answersHashmap = HashMap<String, Boolean>()
    var sparseBoolean = SparseBooleanArray()
    fun updateList(tests: ArrayList<QuestionItem>) {
        this.tests = tests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemTestSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestVH(view.root)
    }

    override fun getItemCount() = tests?.size ?: 0

    override fun onBindViewHolder(holder: TestVH, position: Int) {
//        holder.binding?.testTitle?.text = tests!![position]
        val test = tests!![holder.adapterPosition]
        holder.binding?.question = test
        holder.binding?.testQuestion?.text =
            (position + 1).toString() + "." + tests!![position].text

        holder.binding?.questionToggleGroup?.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (!sparseBoolean.get(position) && isChecked) {

                answersHashmap.put(
                    "" + position + test.id,
                    tests!![holder.adapterPosition].correctAns == holder.itemView.context.resources.getResourceEntryName(
                        checkedId
                    )
                )

                sparseBoolean.put(position, true)
                tests!![holder.adapterPosition].checkedId = checkedId
                tests!![holder.adapterPosition].isChecked = true
            }


        }


        if (sparseBoolean.get(holder.adapterPosition)) {
            holder.binding?.questionToggleGroup?.check(tests!![holder.adapterPosition].checkedId)
        }
        else{
            holder.binding?.A?.isChecked = false
            holder.binding?.B?.isChecked = false
            holder.binding?.C?.isChecked = false
            holder.binding?.D?.isChecked = false

        }
//        else {
//            if (holder.binding?.questionToggleGroup?.checkedButtonId != View.NO_ID) {
//                holder.binding?.questionToggleGroup?.uncheck(
//                    holder.binding?.questionToggleGroup?.checkedButtonId!!
//                )
//            }
//
//        }
//        else {
//           if( holder.binding?.questionToggleGroup?.checkedButtonIds!!.isNotEmpty()){
//               holder.binding?.questionToggleGroup?.clearChecked()
//           }
//        }


    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemTestSetBinding? = DataBindingUtil.bind<ItemTestSetBinding>(itemView)
    }
}