package hu.bme.aut.android.onlab.ui.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.asMavericksArgs
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FavouriteItemBinding
import hu.bme.aut.android.onlab.ui.recipie.RecipeArgs

class FavouriteItemAdapter(): RecyclerView.Adapter<FavouriteItemAdapter.FavouriteItemViewHolder>(){

    class FavouriteItemViewHolder(val binding: FavouriteItemBinding): RecyclerView.ViewHolder(binding.root) {
        val tvFavRecipieNameId: TextView = binding.tvFavRecipieNameId
        val ivRecipiesFlag: ImageView = binding.ivRecipiesFlag
    }

    private var recipies: List<Recipie> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun addRecipie(recipie: Recipie?) {
        recipie ?: return

        recipies += recipie
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteItemViewHolder {
        return FavouriteItemViewHolder(
            FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: FavouriteItemViewHolder,
        position: Int
    ) {
        var cur_item = recipies[position]
        holder.binding.let{ binding ->
            holder.tvFavRecipieNameId.text = cur_item.name
        }
        holder.ivRecipiesFlag.setOnClickListener { view ->
//            val bundle = Bundle()
//            bundle.putString("recipiename", cur_item.name)
            val args = RecipeArgs(cur_item.name!!).asMavericksArgs()
            view.findNavController().navigate(R.id.action_nav_favourites_to_nav_recipie, args)
//            view.findNavController().navigate(R.id.action_nav_favourites_to_nav_recipie, bundle)
        }
    }

    override fun getItemCount(): Int {
        return recipies.size
    }
}