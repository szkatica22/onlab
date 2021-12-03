package hu.bme.aut.android.onlab.ui.recipies

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Flag
import hu.bme.aut.android.onlab.databinding.RecipiesFlagBinding

class FlagItemAdapter(private val context: Context?) :
    RecyclerView.Adapter<FlagItemAdapter.FlagItemViewHolder>(){

    private var flags: List<Flag> = emptyList()

    class FlagItemViewHolder(val binding: RecipiesFlagBinding): RecyclerView.ViewHolder(binding.root){
        val tvFlagTitle: TextView = binding.tvRecipiesFlagTitleId
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addFlag(flag: Flag?){
        flag ?: return

        flags += flag
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteFlag(flag: Flag?){
        flag ?: return

        flags -= flag
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagItemViewHolder {
        return FlagItemViewHolder(
            RecipiesFlagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FlagItemViewHolder, position: Int) {
        var cur_flag = flags[position]
        holder.binding.let { binding ->
            binding.tvRecipiesFlagTitleId.text = cur_flag.name
        }
        holder.binding.ivRecipiesFlag.setOnClickListener{ view ->
            val bundle = Bundle()
            bundle.putString("flag", cur_flag.name)
            view.findNavController().navigate(R.id.action_nav_recipies_to_nav_flag2, bundle)
        }
    }

    override fun getItemCount(): Int {
        return flags.size
    }


}