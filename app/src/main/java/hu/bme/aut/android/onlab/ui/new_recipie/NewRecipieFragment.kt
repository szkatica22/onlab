package hu.bme.aut.android.onlab.ui.new_recipie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.onlab.databinding.FragmentNewRecipieBinding

class NewRecipieFragment : Fragment(){
    private lateinit var newRecipieViewModel: NewRecipieViewModel
    private var _binding: FragmentNewRecipieBinding? = null

    private lateinit var newrecipieController: NewRecipieController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private val inflater: LayoutInflater

    var recipie: NewItem = NewItem("", false)
    var ingredients_list = arrayListOf<String>()
    var preparation_list = arrayListOf<String>()
    var recipie_flags = arrayListOf<String>("Desserst", "Drinks", "Soups", "Main courses")
    var btn_ingredient: String = "Add new ingredient"
    var btn_step: String = "Add new step"
    var btn_save: String = "Save"
    var prep_title: String = "Preparation"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newRecipieViewModel =
            ViewModelProvider(this).get(NewRecipieViewModel::class.java)

        _binding = FragmentNewRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNewRecipie
//        newRecipieViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        newrecipieController = NewRecipieController(recipie, ingredients_list, preparation_list,
        btn_ingredient, prep_title, btn_step, btn_save, inflater)
        binding.ervChangeRecipie.setController(newrecipieController)

        newrecipieController.requestModelBuild()
        binding.ervChangeRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
        RecyclerView.VERTICAL))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}