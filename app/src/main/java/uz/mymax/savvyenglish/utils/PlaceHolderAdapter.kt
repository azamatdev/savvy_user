package uz.mymax.savvyenglish.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import uz.mymax.savvyenglish.R
import uz.mymax.savvyenglish.databinding.ItemTopicBinding
import uz.mymax.savvyenglish.network.response.TopicResponse

class PlaceHolderAdapter(val layoutId: Int) :
    RecyclerView.Adapter<PlaceHolderAdapter.PlaceholderVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceholderVH {
        return PlaceholderVH(
            LayoutInflater.from(parent.context).inflate(layoutId, null)
        )
    }

    override fun getItemCount() = 8

    override fun onBindViewHolder(holder: PlaceHolderAdapter.PlaceholderVH, position: Int) {
    }

    inner class PlaceholderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}