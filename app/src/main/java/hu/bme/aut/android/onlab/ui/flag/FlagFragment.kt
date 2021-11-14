package hu.bme.aut.android.onlab.ui.flag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentFlagBinding

class FlagFragment : Fragment(){
    private lateinit var flagViewModel: FlagViewModel
    private var _binding: FragmentFlagBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recipieitemAdapter: RecipieItemAdapter

    var recipie_list = mutableListOf(RecipieItem("Recipie1"), RecipieItem("Recipie2"), RecipieItem("Recipie3"), RecipieItem("Recipie4"), RecipieItem("Recipie5"), RecipieItem("Recipie6"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flagViewModel =
            ViewModelProvider(this).get(FlagViewModel::class.java)

        _binding = FragmentFlagBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recipieitemAdapter = RecipieItemAdapter(recipie_list)
        binding.rvRecipies.adapter = recipieitemAdapter
        binding.rvRecipies.layoutManager = LinearLayoutManager(this.context)

        val textView: TextView = binding.tvFlagName
        flagViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // New recipie gomb
        binding.fltBtnNewRecipie.setOnClickListener {
            findNavController().navigate(R.id.action_nav_flag2_to_nav_new_recipie)
        }

        // Delete gomb - elozo fragmentre lep at
        binding.btnDeleteFlag.setOnClickListener{
            findNavController().navigate(R.id.action_nav_flag_to_nav_recipies)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}