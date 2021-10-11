package hu.bme.aut.android.onlab.ui.recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import hu.bme.aut.android.onlab.databinding.ItemBinding
import hu.bme.aut.android.onlab.ui.shoppinglist.ListItem

class IngredientItemAdapter(
    private val items: MutableList<Item>
) : RecyclerView.Adapter<IngredientItemAdapter.IngredientItemViewHolder>() {

    class IngredientItemViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: Item) {
        items.add(item)
        notifyItemInserted(items.size-1)
    }

    fun deleteItem(idx: Int) {
        items.removeAt(idx)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): IngredientItemViewHolder {

        return IngredientItemViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientItemViewHolder, position: Int) {
        val cur_item = items[position]
        holder.binding.tvShoppingTitle.text = cur_item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}