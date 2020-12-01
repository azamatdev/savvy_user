package uz.mymax.savvyenglish.ui.topics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_subtopic.view.*
import uz.mymax.savvyenglish.databinding.ItemSubtopicBinding
import uz.mymax.savvyenglish.network.response.SubtopicResponse

class SubtopicsAdapter() :
    RecyclerView.Adapter<SubtopicsAdapter.SubtopicVH>() {

    var itemClickListener: ((Int, String) -> Unit)? = null
    var subtopics: List<SubtopicResponse>? = null

    fun updateList(newList: List<SubtopicResponse>) {
        this.subtopics = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtopicVH {
        val view = ItemSubtopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubtopicVH(view.root)
    }

    override fun getItemCount() = subtopics?.size ?: 0


    override fun onBindViewHolder(holder: SubtopicsAdapter.SubtopicVH, position: Int) {
        var subtopic = subtopics!![position]
        holder.binding?.topic = subtopic

        holder.itemView.item.setOnClickListener {
            itemClickListener?.invoke(subtopic.id, subtopic.title)
        }

        holder.itemView.subtopicTopicsLessonNumber.text = "Dars  ${holder.adapterPosition + 1}"
    }

    inner class SubtopicVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemSubtopicBinding? = DataBindingUtil.bind<ItemSubtopicBinding>(itemView)

    }
}