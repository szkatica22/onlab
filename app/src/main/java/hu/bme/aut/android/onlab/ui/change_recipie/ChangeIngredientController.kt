package hu.bme.aut.android.onlab.ui.change_recipie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.ui.change_recipie.ChangeItem
import hu.bme.aut.android.onlab.databinding.ChangeRecipieItemBinding
import hu.bme.aut.android.onlab.databinding.ItemBinding
import hu.bme.aut.android.onlab.ui.epoxy.ViewBindingKotlinModel
import hu.bme.aut.android.onlab.ui.recipie.Item

class ChangeIngredientController(private var items: ArrayList<ChangeItem> // MutableList<ChangeItem>
) : EpoxyController() {

    fun addItem(item: ChangeItem) {
        items.add(item)
        requestModelBuild()
    }

    fun deleteItem(idx: Int) {
        items.removeAt(idx)
        requestModelBuild()
    }

    override fun buildModels() { //iv_change_recipie_options
        if(items.isEmpty()){
            return
        }
        items.forEach{ item ->
            IngredientEpoxyModel(item).id(item.title).addTo(this)
        }
    }

    data class IngredientEpoxyModel(val ingredient: ChangeItem):
    ViewBindingKotlinModel<ChangeRecipieItemBinding>(R.layout.change_recipie_item){
        override fun ChangeRecipieItemBinding.bind() {
            tvChangeRecipieItemTitleId.text = ingredient.title
//            tvShoppingTitle.text = ingredient.title
        }
    }



//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeIngredientViewHolder {
//
//        return ChangeIngredientViewHolder(
//            ChangeRecipieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ChangeIngredientViewHolder, position: Int) {
//        val cur_item = items[position]
//        holder.binding.tvChangeRecipieItemTitleId.text = cur_item.title
////        holder.binding.tvShoppingTitle.text = cur_item.title
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
}