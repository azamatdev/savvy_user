package uz.mymax.savvyenglish.ui.topics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.R

class TopicsPlaceholderAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic_placeholder, parent, false)
        return TopicPlaceHolder(view)
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    class TopicPlaceHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}