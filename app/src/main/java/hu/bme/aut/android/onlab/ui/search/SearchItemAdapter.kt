package hu.bme.aut.android.onlab.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.SearchItemBinding

class SearchItemAdapter (
    private val context: Context?
) : RecyclerView.Adapter<SearchItemAdapter.SearchViewHolder>() {

    class SearchViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root)

//    private val db = Firebase.firestore
    private var list: List<Recipie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Recipie) {
        list += item
        notifyDataSetChanged()
    }

//    @SuppressLint("NotifyDataSetChanged")
    fun clearList(){
        list = emptyList()
//        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val cur_item = list[position]
        holder.binding.tvRecipieNameId.text = cur_item.name
        holder.binding.ivRecipie.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putString("recipiename", cur_item.name)
            view.findNavController().navigate(R.id.action_searchFragment_to_nav_recipie, bundle)
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}