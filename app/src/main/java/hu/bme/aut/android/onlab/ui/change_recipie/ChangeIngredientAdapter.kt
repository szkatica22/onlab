package hu.bme.aut.android.onlab.ui.change_recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.ui.change_recipie.ChangeItem
import hu.bme.aut.android.onlab.databinding.ChangeRecipieItemBinding

class ChangeIngredientAdapter (private val items: MutableList<ChangeItem>
) : RecyclerView.Adapter<ChangeIngredientAdapter.ChangeIngredientViewHolder>() {

    class ChangeIngredientViewHolder(val binding: ChangeRecipieItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: ChangeItem) {
        items.add(item)
        notifyItemInserted(items.size-1)
    }

    fun deleteItem(idx: Int) {
        items.removeAt(idx)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeIngredientViewHolder {

        return ChangeIngredientViewHolder(
            ChangeRecipieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChangeIngredientViewHolder, position: Int) {
        val cur_item = items[position]
        holder.binding.tvChangeRecipieItemTitleId.text = cur_item.title
//        holder.binding.tvShoppingTitle.text = cur_item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}