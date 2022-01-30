package hu.bme.aut.android.onlab.ui.shoppinglist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.ShoppingItem
import hu.bme.aut.android.onlab.databinding.ItemShoppingListBinding

class ListItemAdapter (
    private val context: Context?
) : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    class ListItemViewHolder(val binding: ItemShoppingListBinding) : RecyclerView.ViewHolder(binding.root)

    private val db = Firebase.firestore
    private var list: List<ShoppingItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            ItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: ShoppingItem) {
        list += item
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePurchasedItems() {
        for(item in list){
            if(item.checked == true){
                list = list.minusElement(item)
            }
        }
        notifyDataSetChanged()
    }

    fun toggleStrikeThrough(tvShoppingTitle: TextView, tvQuantity: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvShoppingTitle.paintFlags = tvShoppingTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            tvQuantity.paintFlags = tvQuantity.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvShoppingTitle.paintFlags = tvShoppingTitle.paintFlags and  STRIKE_THRU_TEXT_FLAG.inv()
            tvQuantity.paintFlags = tvQuantity.paintFlags and  STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun changeChecked(item: ShoppingItem){
        db.collection("shopping_list").whereEqualTo("name", item.name).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                if(snapshot.documents.isNotEmpty()){
                    db.collection("shopping_list").document(snapshot.documents[0].id).update("checked", item.checked)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val cur_item = list[position]
        holder.binding.let { binding ->
            binding.tvShoppingTitle.text = cur_item.name
            var tmp_quant_unit: String? = null
            if (cur_item.quantity == null){
                tmp_quant_unit = "-"
            } else {
                tmp_quant_unit = cur_item.quantity.toString()
            }
            if ( cur_item.unit == null){
                tmp_quant_unit += " -"
            } else {
                tmp_quant_unit += " " + cur_item.unit
            }
            binding.tvQuantity.text = tmp_quant_unit
            binding.cbPurchased.isChecked = cur_item.checked == true
            toggleStrikeThrough(binding.tvShoppingTitle, binding.tvQuantity,cur_item.checked == true)

            binding.cbPurchased.setOnCheckedChangeListener { _, is_checked ->
                toggleStrikeThrough(binding.tvShoppingTitle, binding.tvQuantity, is_checked)
                cur_item.checked = !cur_item.checked!!
                changeChecked(cur_item)
            }
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}