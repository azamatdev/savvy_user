package uz.mymax.savvyenglish.ui.tests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_topic.view.*
import uz.mymax.savvyenglish.databinding.ItemThemeTestBinding
import uz.mymax.savvyenglish.network.response.ThemeTestResponse

class ThemeTestAdapter : RecyclerView.Adapter<ThemeTestAdapter.TestVH>() {

    var itemClickListener: ((Int) -> Unit)? = null
    var onLongClickListener: ((ThemeTestResponse) -> Unit)? = null
    var themeList: ArrayList<ThemeTestResponse>? = null

    fun updateList(newList: List<ThemeTestResponse>) {
        this.themeList = newList as ArrayList<ThemeTestResponse>;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestVH {
        val view = ItemThemeTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestVH(view.root)
    }

    override fun getItemCount() = themeList?.size ?: 0

    override fun onBindViewHolder(holder: TestVH, position: Int) {
        holder.binding?.test = themeList!![position]

        holder.itemView.child.setOnClickListener {
            itemClickListener?.invoke(themeList!![position].id)
        }
        holder.itemView.child.setOnLongClickListener {
            onLongClickListener?.invoke(themeList!![position])
            return@setOnLongClickListener true
        }

    }

    inner class TestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemThemeTestBinding? = DataBindingUtil.bind<ItemThemeTestBinding>(itemView)
    }
}