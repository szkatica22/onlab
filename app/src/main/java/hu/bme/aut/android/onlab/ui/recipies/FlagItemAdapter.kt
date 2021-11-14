package hu.bme.aut.android.onlab.ui.recipies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.RecipiesFlagBinding

class FlagItemAdapter (
    private val flags: MutableList<FlagItem>
): RecyclerView.Adapter<FlagItemAdapter.FlagItemViewHolder>(){

    class FlagItemViewHolder(val binding: RecipiesFlagBinding): RecyclerView.ViewHolder(binding.root)

    fun addFlag(flag: FlagItem){
        flags.add(flag)
        notifyItemInserted(flags.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagItemViewHolder {
        return FlagItemViewHolder(
            RecipiesFlagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FlagItemViewHolder, position: Int) {
        var cur_flag = flags[position]
        holder.binding.let { binding ->
            binding.tvRecipiesFlagTitleId.text = cur_flag.title
        }
        holder.binding.ivRecipiesFlag.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.action_nav_recipies_to_nav_flag2)
        }
    }

    override fun getItemCount(): Int {
        return flags.size
    }


}