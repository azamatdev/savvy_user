package uz.mymax.savvyenglish.ui.topics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_subtopic.view.*
import uz.mymax.savvyenglish.databinding.ItemSubtopicBinding
import uz.mymax.savvyenglish.network.response.SubtopicResponse
import uz.mymax.savvyenglish.network.response.TopicResponse

class SubtopicsAdapter() :
    RecyclerView.Adapter<SubtopicsAdapter.SubtopicVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var topicList: List<SubtopicResponse>? = null

    fun updateList(newList: List<SubtopicResponse>) {
        this.topicList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtopicVH {
        val view = ItemSubtopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubtopicVH(view.root)
    }

    override fun getItemCount() = topicList?.size ?: 0


    override fun onBindViewHolder(holder: SubtopicsAdapter.SubtopicVH, position: Int) {
        holder.binding?.topic = topicList!![position]

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(topicList!![position].id)
        }

        holder.itemView.subtopicTopicsLessonNumber.text = "Dars  ${holder.adapterPosition + 1}"
    }

    inner class SubtopicVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemSubtopicBinding? = DataBindingUtil.bind<ItemSubtopicBinding>(itemView)

    }
}