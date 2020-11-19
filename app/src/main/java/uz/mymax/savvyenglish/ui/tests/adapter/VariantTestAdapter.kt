package uz.mymax.savvyenglish.ui.tests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import uz.mymax.savvyenglish.databinding.ItemTopicBinding
import uz.mymax.savvyenglish.databinding.ItemTopicTestBinding
import uz.mymax.savvyenglish.databinding.ItemVariantBinding
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.network.response.TopicTestResponse
import uz.mymax.savvyenglish.network.response.VariantTestResponse

class VariantTestAdapter : RecyclerView.Adapter<VariantTestAdapter.TestVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var topicList: ArrayList<VariantTestResponse>? = null

    fun updateList(newList: List<VariantTestResponse>) {
        this.topicList = newList as ArrayList<VariantTestResponse>;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemVariantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestVH(view.root)
    }

    override fun getItemCount() = topicList?.size ?: 0

    override fun onBindViewHolder(holder: TestVH, position: Int) {
        holder.binding?.variant = topicList!![position]

        holder.itemView.child.setOnClickListener {
            itemClickListener?.invoke(topicList!![position].id)
        }
    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemVariantBinding? = DataBindingUtil.bind<ItemVariantBinding>(itemView)
    }
}