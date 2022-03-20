package hu.bme.aut.android.onlab.ui.shares

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.asMavericksArgs
import hu.bme.aut.android.onlab.data.RecipeArgs
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.EpoxySharedItemBinding

class SharedItemAdapter(): RecyclerView.Adapter<SharedItemAdapter.SharedItemViewHolder>(){

    private var recipies: List<Recipie> = emptyList()

    class SharedItemViewHolder(val binding: EpoxySharedItemBinding): RecyclerView.ViewHolder(binding.root) {
        val tvFavRecipieNameId: TextView = binding.tvFavRecipieNameId
        val ivRecipiesFlag: ImageView = binding.ivRecipiesFlag
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRecipie(recipie: Recipie?) {
        recipie ?: return

        recipies += recipie
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteRecipie(recipie: Recipie?){
        recipie ?: return

        recipies -= recipie
//        notifyItemRemoved(recipies.indexOf(recipie))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SharedItemViewHolder {
        return SharedItemViewHolder(
            EpoxySharedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: SharedItemViewHolder,
        position: Int
    ) {
        var cur_item = recipies[position]
        holder.binding.let{ binding ->
            holder.tvFavRecipieNameId.text = cur_item.name
        }
        holder.ivRecipiesFlag.setOnClickListener { view ->
            val bundle = Bundle()
//            bundle.putString("recipiename", cur_item.name)
            val args = RecipeArgs(cur_item.name!!).asMavericksArgs()
            view.findNavController().navigate(R.id.action_nav_shares_to_nav_shared_recipie, args)
//            view.findNavController().navigate(R.id.action_nav_shares_to_nav_shared_recipie, bundle)
        }
    }

    override fun getItemCount(): Int {
        return recipies.size
    }
}