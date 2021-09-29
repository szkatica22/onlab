package hu.bme.aut.android.onlab.ui.shoppinglist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
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

    private lateinit var binding: ItemShoppingListBinding

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {

        binding = ItemShoppingListBinding.inflate(LayoutInflater.from(parent.context))

        return ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping_list, parent, false
            )
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
        holder.itemView.apply {
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