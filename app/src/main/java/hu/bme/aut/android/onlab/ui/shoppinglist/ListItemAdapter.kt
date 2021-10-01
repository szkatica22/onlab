package hu.bme.aut.android.onlab.ui.shoppinglist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.ItemShoppingListBinding

class ListItemAdapter (
    private val items: MutableList<ListItem>
) : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    class ListItemViewHolder(val binding: ItemShoppingListBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            ItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun addItem(item: ListItem) {
        items.add(item)
        notifyItemInserted(items.size-1)
    }

    fun deletePurchasedItems() {
        items.removeAll { item ->
            item.is_checked
        }
        notifyDataSetChanged()
    }

    fun toggleStrikeThrough(tvShoppingTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvShoppingTitle.paintFlags = tvShoppingTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvShoppingTitle.paintFlags = tvShoppingTitle.paintFlags and  STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        var cur_item = items[position]
        holder.binding.let { binding ->
            binding.tvShoppingTitle.text = cur_item.title
            binding.cbPurchased.isChecked = cur_item.is_checked
            toggleStrikeThrough(binding.tvShoppingTitle, cur_item.is_checked)

            binding.cbPurchased.setOnCheckedChangeListener { _, is_checked ->
                toggleStrikeThrough(binding.tvShoppingTitle, is_checked)
                cur_item.is_checked = !cur_item.is_checked
            }
        }
    }

    override fun getItemCount(): Int {
        return  items.size
    }
}