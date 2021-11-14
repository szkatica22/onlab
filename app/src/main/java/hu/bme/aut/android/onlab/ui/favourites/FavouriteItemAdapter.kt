package hu.bme.aut.android.onlab.ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FavouriteItemBinding

class FavouriteItemAdapter(
    private val favourites: MutableList<FavouriteItem>
): RecyclerView.Adapter<FavouriteItemAdapter.FavouriteItemViewHolder>(){

    class FavouriteItemViewHolder(val binding: FavouriteItemBinding): RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteItemAdapter.FavouriteItemViewHolder {
        return FavouriteItemAdapter.FavouriteItemViewHolder(
            FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: FavouriteItemAdapter.FavouriteItemViewHolder,
        position: Int
    ) {
        var cur_item = favourites[position]
        holder.binding.let{ binding ->
            binding.tvFavRecipieNameId.text = cur_item.title
        }
        holder.binding.ivRecipiesFlag.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_nav_favourites_to_nav_recipie)
        }
    }

    override fun getItemCount(): Int {
        return favourites.size
    }
}