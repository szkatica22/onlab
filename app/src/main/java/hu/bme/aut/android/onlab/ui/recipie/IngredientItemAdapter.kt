package hu.bme.aut.android.onlab.ui.recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import hu.bme.aut.android.onlab.databinding.ItemBinding

class IngredientItemAdapter(
    private val items: MutableList<Item>
) : RecyclerView.Adapter<IngredientItemAdapter.IngredientItemViewHolder>() {

//    val ingredients = arrayOf("1 cup espresso", "6 egg yolks", "6 Tbsp rum", "30 ladyfingers",
//            "3/4 cup sugar", "425 g mascarpone", "2 cups heavy whipping cream", "2-3 TBSP cocoa powder")


    class IngredientItemViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): IngredientItemViewHolder {
        // Test items
//        items.add(Item("1 cup espresso"))
//        items.add(Item("6 egg yolks"))
//        items.add(Item("6 Tbsp rum"))
//        items.add(Item("30 ladyfingers"))
        return IngredientItemViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientItemViewHolder, position: Int) {
        var cur_item = items[position]
        holder.binding.tvShoppingTitle.text = cur_item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}