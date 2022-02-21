package hu.bme.aut.android.onlab.ui.flag

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.RecipieItemBinding

class RecipieItemAdapter (private val context: Context?): RecyclerView.Adapter<RecipieItemAdapter.RecipieItemViewHolder>(){

    private var recipies: List<Recipie> = emptyList()

    class RecipieItemViewHolder(val binding: RecipieItemBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun addRecipie(recipie: Recipie?) {
        recipie ?: return

        recipies += recipie
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipieItemViewHolder {
        return RecipieItemAdapter.RecipieItemViewHolder(
            RecipieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipieItemViewHolder, position: Int) {
        var cur_recipie = recipies[position]
        holder.binding.let { binding ->
            binding.tvRecipieNameId.text = cur_recipie.name
        }
        holder.binding.ivRecipiesFlag.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putString("recipiename", cur_recipie.name)
            view.findNavController().navigate(R.id.action_nav_flag_to_nav_recipie, bundle)
        }
    }

    override fun getItemCount(): Int {
        return recipies.size
    }
}