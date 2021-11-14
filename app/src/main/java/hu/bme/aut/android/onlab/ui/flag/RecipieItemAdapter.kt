package hu.bme.aut.android.onlab.ui.flag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.RecipieItemBinding
import hu.bme.aut.android.onlab.databinding.RecipiesFlagBinding
import hu.bme.aut.android.onlab.ui.recipies.FlagItemAdapter

class RecipieItemAdapter (
    private val recipies: MutableList<RecipieItem>
): RecyclerView.Adapter<RecipieItemAdapter.RecipieItemViewHolder>(){

    class RecipieItemViewHolder(val binding: RecipieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipieItemViewHolder {
        return RecipieItemAdapter.RecipieItemViewHolder(
            RecipieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipieItemViewHolder, position: Int) {
        var cur_recipie = recipies[position]
        holder.binding.let { binding ->
            binding.tvRecipieNameId.text = cur_recipie.title
        }
        holder.binding.ivRecipiesFlag.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_nav_flag_to_nav_recipie)
        }
    }

    override fun getItemCount(): Int {
        return recipies.size
    }
}