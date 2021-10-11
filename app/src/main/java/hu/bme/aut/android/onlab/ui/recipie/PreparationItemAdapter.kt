package hu.bme.aut.android.onlab.ui.recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.databinding.ItemBinding
import hu.bme.aut.android.onlab.databinding.ItemShoppingListBinding

class PreparationItemAdapter(
    private val items: MutableList<Item>
) : RecyclerView.Adapter<PreparationItemAdapter.PreparationItemViewHolder>() {

    class PreparationItemViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): PreparationItemViewHolder {

        return PreparationItemViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: PreparationItemViewHolder,
        position: Int
    ) {
        val cur_item = items[position]
        holder.binding.tvShoppingTitle.text = cur_item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}