package hu.bme.aut.android.onlab.ui.shoppinglist

import android.annotation.SuppressLint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.ShoppingItem
import hu.bme.aut.android.onlab.databinding.ItemShoppingListBinding

class ListItemAdapter (
    private val items: MutableList<ListItem>
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
//        items.add(item)
//        notifyItemInserted(items.size-1)
        list += item
        Log.d("LIST: ", list.toString())
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePurchasedItems() {
        for(i in list.indices){
            if(list[i].checked == true){
                list.drop(i)
            }
        }
//        items.removeAll { item ->
//            item.is_checked
//        }
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
        var cur_item = list[position]
        holder.binding.let { binding ->
            binding.tvShoppingTitle.text = cur_item.name
            binding.cbPurchased.isChecked = cur_item.checked == true
            toggleStrikeThrough(binding.tvShoppingTitle, cur_item.checked == true)

            binding.cbPurchased.setOnCheckedChangeListener { _, is_checked ->
                toggleStrikeThrough(binding.tvShoppingTitle, is_checked)
                cur_item.checked = !cur_item.checked!!
            }
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}