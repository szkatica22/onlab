package hu.bme.aut.android.onlab.ui.recipie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentRecipieBinding

class RecipieFragment : Fragment(){
    private lateinit var recipieViewModel: RecipieViewModel
    private var _binding: FragmentRecipieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipieViewModel =
            ViewModelProvider(this).get(RecipieViewModel::class.java)

        _binding = FragmentRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.tvRecipieName
        recipieViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // Teszt gomb a kovi fragmentre valtashoz
        binding.imgBtnEdit.setOnClickListener{
            findNavController().navigate(R.id.action_nav_recipie_to_nav_change_recipie)
        }


        // Recipie Ingredients
        val ingr_array_adapter: ArrayAdapter<*>
        val ingredients = arrayOf("1 cup espresso", "6 egg yolks", "6 Tbsp rum", "30 ladyfingers",
            "3/4 cup sugar", "425 g mascarpone", "2 cups heavy whipping cream", "2-3 TBSP cocoa powder")

        var ingr_list_view = binding.lvRecipieIngredients
        ingr_array_adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, ingredients) }!!
        ingr_list_view.adapter = ingr_array_adapter

        // Recipie Preparation
        val prep_array_adapter: ArrayAdapter<*>
        val steps = arrayOf("step1", "step2", "step3", "step4", "step5")

        var prep_list_view = binding.lvRecipiePreparation
        prep_array_adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, steps) }!!
        prep_list_view.adapter = prep_array_adapter


        // Recept flag-k a spinner-hez ideiglenes ertekek
        val flags = arrayOf("Desserts", "Main courses", "Soups", "Drinks")
        val flags_array_adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, flags) }!!

        // attached arrayadapter to spinner
        binding.spnrRecipieCategory.adapter = flags_array_adapter

//        binding.spnrRecipieCategory.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                binding.tvRecipieSpinnerSelected.text = flags[p2]
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//
//        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}