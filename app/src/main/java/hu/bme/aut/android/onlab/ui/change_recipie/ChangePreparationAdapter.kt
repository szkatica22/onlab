package hu.bme.aut.android.onlab.ui.change_recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.databinding.ChangeRecipieItemBinding

class ChangePreparationAdapter(private val items: MutableList<ChangeItem>
) : RecyclerView.Adapter<ChangePreparationAdapter.ChangePreparationViewHolder>() {

    class ChangePreparationViewHolder(val binding: ChangeRecipieItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun addItem(item: ChangeItem) {
        items.add(item)
        notifyItemInserted(items.size-1)
    }

    fun deleteItem(idx: Int) {
        items.removeAt(idx)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangePreparationViewHolder {

        return ChangePreparationViewHolder(
            ChangeRecipieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChangePreparationViewHolder, position: Int) {
        val cur_item = items[position]
        holder.binding.tvChangeRecipieItemTitleId.text = cur_item.title
//        holder.binding.tvShoppingTitle.text = cur_item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}