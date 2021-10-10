package hu.bme.aut.android.onlab.ui.recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.databinding.ItemBinding
import hu.bme.aut.android.onlab.databinding.ItemShoppingListBinding

class PreparationItemAdapter(
    private val items: MutableList<Item>
) : RecyclerView.Adapter<PreparationItemAdapter.PreparationItemViewHolder>() {

//    val steps = arrayOf("step1", "step2", "step3", "step4", "step5")

    class PreparationItemViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): PreparationItemViewHolder {
        // Test items
//        items.add(Item("step1"))
//        items.add(Item("step2"))
//        items.add(Item("step3"))
//        items.add(Item("step4"))

        return PreparationItemViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: PreparationItemViewHolder,
        position: Int
    ) {
        var cur_item = items[position]
        holder.binding.tvShoppingTitle.text = cur_item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}