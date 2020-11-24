package uz.mymax.savvyenglish.ui.tests.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import uz.mymax.savvyenglish.databinding.ItemTopicBinding
import uz.mymax.savvyenglish.databinding.ItemTopicTestBinding
import uz.mymax.savvyenglish.network.response.TopicResponse
import uz.mymax.savvyenglish.network.response.TopicTestResponse

class TopicTestAdapter : RecyclerView.Adapter<TopicTestAdapter.TestVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var topicList: ArrayList<TopicTestResponse>? = null

    fun updateList(newList: List<TopicTestResponse>) {
        this.topicList = newList as ArrayList<TopicTestResponse>;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemTopicTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestVH(view.root)
    }

    override fun getItemCount() = topicList?.size ?: 0

    override fun onBindViewHolder(holder: TestVH, position: Int) {
        holder.binding?.test = topicList!![position]

        holder.itemView.child.setOnClickListener {
            itemClickListener?.invoke(topicList!![position].id)
        }

    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemTopicTestBinding? = DataBindingUtil.bind<ItemTopicTestBinding>(itemView)
    }
}