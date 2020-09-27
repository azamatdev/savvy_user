package uz.mymax.savvyenglish.ui.topics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import uz.mymax.savvyenglish.databinding.ItemTopicBinding
import uz.mymax.savvyenglish.network.response.TopicResponse

class TopicsAdapter() :
    RecyclerView.Adapter<TopicsAdapter.TopicsVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var topicList: ArrayList<TopicResponse>? = null

    fun updateList(newList: List<TopicResponse>) {
        this.topicList = newList as ArrayList<TopicResponse>;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsVH {
        val view = ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicsVH(view.root)
    }

    override fun getItemCount() = topicList?.size ?: 0


    override fun onBindViewHolder(holder: TopicsAdapter.TopicsVH, position: Int) {
        holder.binding?.topic = topicList!![position]

        holder.itemView.child.setOnClickListener {
            itemClickListener?.invoke(topicList!![position].id)
        }
    }

    inner class TopicsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemTopicBinding? = DataBindingUtil.bind<ItemTopicBinding>(itemView)

    }
}