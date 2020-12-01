package uz.mymax.savvyenglish.ui.tests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import uz.mymax.savvyenglish.databinding.ItemVariantBinding
import uz.mymax.savvyenglish.network.response.VariantTestResponse
import uz.mymax.savvyenglish.utils.isAdmin

class VariantAdapter : RecyclerView.Adapter<VariantAdapter.TestVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var onLongClickListener: ((VariantTestResponse) -> Unit)? = null

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
        holder.itemView.child.setOnLongClickListener {
            if (holder.itemView.context.isAdmin())
                onLongClickListener?.invoke(topicList!![position])
            return@setOnLongClickListener true
        }
    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemVariantBinding? = DataBindingUtil.bind<ItemVariantBinding>(itemView)
    }
}