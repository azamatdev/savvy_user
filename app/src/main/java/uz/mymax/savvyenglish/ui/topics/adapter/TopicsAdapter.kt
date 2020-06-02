package uz.mymax.savvyenglish.ui.topics.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.model.Topic
import uz.mymax.savvyenglish.model.TopicHeader
import uz.mymax.savvyenglish.model.TopicType
import uz.mymax.savvyenglish.databinding.ItemTopicsBinding
import uz.mymax.savvyenglish.databinding.ItemTopicsHeaderBinding

class TopicsAdapter(val callback : (Int) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaderInterface {

    var topicList = ArrayList<TopicType>()
    private val HEADER_TYPE = 1

    fun updateList(newList : List<TopicType>){
        this.topicList = newList as ArrayList<TopicType>;
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return topicList[position].type()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_TYPE) {
            val view =
                ItemTopicsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TopicsHeaderVH(view.root)
        } else {
            val view = ItemTopicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TopicsVH(view.root)
        }
    }

    override fun getItemCount() = topicList.size

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = 0
        var itemPositionVar = itemPosition
        do{
            if(this.isHeader(itemPositionVar)){
                headerPosition = itemPositionVar
                break
            }
            itemPositionVar -= 1

        }while (itemPositionVar >= 0)
        return headerPosition
    }
    override fun isHeader(itemPosition: Int): Boolean {
        return topicList[itemPosition].type() == 1
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.item_topics_header
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        header!!.findViewById<TextView>(R.id.txtTopicsHeader).text = (topicList[headerPosition] as TopicHeader).title
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TopicsVH -> {
                holder.binding!!.topic = topicList[position] as Topic
                holder.bindColors(position)
                holder.binding!!.child.setOnClickListener {
                    Log.d("TagCheck","AdapterClick")
                    callback(holder.adapterPosition) }
            }
            is TopicsHeaderVH -> {
                holder.binding!!.txtTopicsHeader.text = (topicList[position] as TopicHeader).title
            }
        }
    }

    inner class TopicsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : ItemTopicsBinding? = DataBindingUtil.bind<ItemTopicsBinding>(itemView)
        fun bindColors(position: Int){
            binding!!.topicLeftView.background = when (position % 7) {
                0 -> itemView.context.getDrawable(R.drawable.bg_left_rounded)
                1 -> itemView.context.getDrawable(R.drawable.bg_left_rounded2)
                2 -> itemView.context.getDrawable(R.drawable.bg_left_rounded3)
                3 -> itemView.context.getDrawable(R.drawable.bg_left_rounded4)
                4 -> itemView.context.getDrawable(R.drawable.bg_left_rounded5)
                5 -> itemView.context.getDrawable(R.drawable.bg_left_rounded6)
                6 -> itemView.context.getDrawable(R.drawable.bg_left_rounded7)
                else
                -> itemView.context.getDrawable(R.drawable.bg_left_rounded)
            }

        }
    }

    inner class TopicsHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : ItemTopicsHeaderBinding? = DataBindingUtil.bind<ItemTopicsHeaderBinding>(itemView)
    }

}