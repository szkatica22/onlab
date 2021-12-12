package hu.bme.aut.android.onlab.ui.shoppinglist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    fun toggleStrikeThrough(tvShoppingTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvShoppingTitle.paintFlags = tvShoppingTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvShoppingTitle.paintFlags = tvShoppingTitle.paintFlags and  STRIKE_THRU_TEXT_FLAG.inv()
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
            binding.cbPurchased.isChecked = cur_item.checked == true
            toggleStrikeThrough(binding.tvShoppingTitle, cur_item.checked == true)

            binding.cbPurchased.setOnCheckedChangeListener { _, is_checked ->
                toggleStrikeThrough(binding.tvShoppingTitle, is_checked)
                cur_item.checked = !cur_item.checked!!
                changeChecked(cur_item)
            }
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}