package uz.mymax.savvyenglish.ui.tests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.databinding.ItemTestSetBinding

class TestSetAdapter : RecyclerView.Adapter<TestSetAdapter.TestVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var tests: ArrayList<String>? = null

    init {
        tests = ArrayList()
        tests?.add("Test1")
        tests?.add("Test2")
        tests?.add("Test3")
        tests?.add("Test4")
        tests?.add("Test5")
        tests?.add("Test6")
        tests?.add("Test7")
        tests?.add("Test8")
        tests?.add("Test9")
        tests?.add("Test10")
        tests?.add("Test11")
        tests?.add("Test12")
        tests?.add("Test13")
        tests?.add("Test14")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemTestSetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestVH(view.root)
    }

    override fun getItemCount() = tests?.size ?: 0

    override fun onBindViewHolder(holder: TestVH, position: Int) {
//        holder.binding?.testTitle?.text = tests!![position]
        holder.binding?.testTitle?.text = "1.Choose the correct answer"
        holder.binding?.testQuestion?.text = "More wild animals ... extinct these days"
    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemTestSetBinding? = DataBindingUtil.bind<ItemTestSetBinding>(itemView)
    }
}